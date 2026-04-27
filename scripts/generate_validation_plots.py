from __future__ import annotations

import csv
import math
import subprocess
from dataclasses import dataclass
from pathlib import Path

import matplotlib.pyplot as plt
import numpy as np


EPS = 1e-6


@dataclass(frozen=True)
class ModulePlotConfig:
    module: str
    x_from: float
    x_to: float
    step: float
    title: str


MODULES: tuple[ModulePlotConfig, ...] = (
    ModulePlotConfig("sin", -2.0 * math.pi, 2.0 * math.pi, 0.02, "sin(x)"),
    ModulePlotConfig("cos", -2.0 * math.pi, 2.0 * math.pi, 0.02, "cos(x)"),
    ModulePlotConfig("sec", -2.0 * math.pi, 2.0 * math.pi, 0.02, "sec(x)"),
    ModulePlotConfig("ln", 0.1, 5.0, 0.01, "ln(x)"),
    ModulePlotConfig("log2", 0.1, 5.0, 0.01, "log2(x)"),
    ModulePlotConfig("log5", 0.1, 5.0, 0.01, "log5(x)"),
    ModulePlotConfig("system", -2.0, 5.0, 0.01, "system(x)"),
)


def run_gradle_export(project_root: Path, config: ModulePlotConfig, output_csv: Path) -> None:
    gradlew = project_root / "gradlew"
    args_value = (
        f"--module {config.module} "
        f"--from {config.x_from} "
        f"--to {config.x_to} "
        f"--step {config.step} "
        f"--eps {EPS} "
        f"--out {output_csv} "
        "--delimiter ;"
    )

    cmd = [str(gradlew), "run", f"--args={args_value}"]
    subprocess.run(cmd, cwd=project_root, check=True)


def load_csv_values(csv_path: Path) -> tuple[np.ndarray, np.ndarray]:
    x_values: list[float] = []
    y_values: list[float] = []

    with csv_path.open("r", encoding="utf-8", newline="") as f:
        reader = csv.DictReader(f, delimiter=";")
        for row in reader:
            x_values.append(float(row["x"]))
            y_values.append(float(row["result"]))

    return np.array(x_values, dtype=float), np.array(y_values, dtype=float)


def expected_value(module: str, x: float) -> float:
    if module == "sin":
        return math.sin(x)

    if module == "cos":
        return math.cos(x)

    if module == "sec":
        cos_x = math.cos(x)
        if abs(cos_x) < EPS:
            return math.nan
        return 1.0 / cos_x

    if module == "ln":
        if x <= 0.0:
            return math.nan
        return math.log(x)

    if module == "log2":
        if x <= 0.0:
            return math.nan
        return math.log2(x)

    if module == "log5":
        if x <= 0.0:
            return math.nan
        return math.log(x, 5)

    if module == "system":
        if x <= 0.0:
            cos_x = math.cos(x)
            if abs(cos_x) < EPS:
                return math.nan
            sec_x = 1.0 / cos_x
            left = ((((cos_x * sec_x) * math.sin(x)) * sec_x) * cos_x)
            return left * left

        log2_x = math.log2(x)
        log5_x = math.log(x, 5)
        if abs(log5_x) < EPS:
            return math.nan
        numerator = ((log2_x + log5_x + log2_x) ** 3) ** 2
        return numerator / log5_x

    raise ValueError(f"Unsupported module: {module}")


def calc_metrics(actual: np.ndarray, expected: np.ndarray) -> dict[str, float]:
    finite_mask = np.isfinite(actual) & np.isfinite(expected)
    if not np.any(finite_mask):
        return {
            "count": 0,
            "max_abs_error": float("nan"),
            "mean_abs_error": float("nan"),
        }

    diff = np.abs(actual[finite_mask] - expected[finite_mask])
    return {
        "count": int(diff.size),
        "max_abs_error": float(np.max(diff)),
        "mean_abs_error": float(np.mean(diff)),
    }


def create_plot(
    config: ModulePlotConfig,
    x_values: np.ndarray,
    actual: np.ndarray,
    expected: np.ndarray,
    image_path: Path,
) -> None:
    abs_error = np.abs(actual - expected)

    fig, (ax_fn, ax_err) = plt.subplots(
        2,
        1,
        figsize=(11, 8),
        sharex=True,
        gridspec_kw={"height_ratios": [3, 1]},
    )

    ax_fn.plot(x_values, expected, color="#1f77b4", linewidth=2.0, label="Эталон")
    ax_fn.plot(
        x_values,
        actual,
        color="#d62728",
        linewidth=1.5,
        linestyle="--",
        label="Реализация проекта",
    )
    ax_fn.set_title(f"{config.title}: реализация vs эталон")
    ax_fn.set_ylabel("y")
    ax_fn.grid(alpha=0.25)
    ax_fn.legend()

    finite_err_mask = np.isfinite(abs_error)
    if np.any(finite_err_mask):
        ax_err.plot(x_values[finite_err_mask], abs_error[finite_err_mask], color="#2ca02c")
    else:
        ax_err.text(0.5, 0.5, "Нет точек для оценки ошибки", ha="center", va="center")

    ax_err.set_ylabel("|delta|")
    ax_err.set_xlabel("x")
    ax_err.grid(alpha=0.25)

    fig.tight_layout()
    fig.savefig(image_path, dpi=180)
    plt.close(fig)


def write_metrics_table(output_path: Path, metrics: list[tuple[str, dict[str, float]]]) -> None:
    lines = [
        "# Метрики сравнения реализации с эталоном",
        "",
        "| Модуль | Кол-во валидных точек | Max |delta| | Mean |delta| |",
        "|---|---:|---:|---:|",
    ]

    for module, metric in metrics:
        if metric["count"] == 0:
            max_err = "n/a"
            mean_err = "n/a"
        else:
            max_err = f"{metric['max_abs_error']:.6e}"
            mean_err = f"{metric['mean_abs_error']:.6e}"

        lines.append(f"| {module} | {metric['count']} | {max_err} | {mean_err} |")

    output_path.write_text("\n".join(lines) + "\n", encoding="utf-8")


def main() -> None:
    project_root = Path(__file__).resolve().parents[1]

    plots_dir = project_root / "docs" / "plots"
    data_dir = plots_dir / "data"
    plots_dir.mkdir(parents=True, exist_ok=True)
    data_dir.mkdir(parents=True, exist_ok=True)

    all_metrics: list[tuple[str, dict[str, float]]] = []

    for module_config in MODULES:
        csv_path = data_dir / f"{module_config.module}.csv"
        image_path = plots_dir / f"{module_config.module}.png"

        run_gradle_export(project_root, module_config, csv_path)
        x_values, actual_values = load_csv_values(csv_path)
        expected_values = np.array(
            [expected_value(module_config.module, x) for x in x_values],
            dtype=float,
        )

        metrics = calc_metrics(actual_values, expected_values)
        all_metrics.append((module_config.module, metrics))

        create_plot(module_config, x_values, actual_values, expected_values, image_path)
        print(f"Generated: {image_path}")

    metrics_path = plots_dir / "metrics.md"
    write_metrics_table(metrics_path, all_metrics)
    print(f"Generated: {metrics_path}")


if __name__ == "__main__":
    main()
