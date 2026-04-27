package tpo.lab2.stubs

import tpo.lab2.functions.FunctionModule

class SinStub(table: Map<Double, Double>, tolerance: Double = 1e-9) :
    FunctionModule by TableFunctionStub(table, tolerance)

class CosStub(table: Map<Double, Double>, tolerance: Double = 1e-9) :
    FunctionModule by TableFunctionStub(table, tolerance)

class SecStub(table: Map<Double, Double>, tolerance: Double = 1e-9) :
    FunctionModule by TableFunctionStub(table, tolerance)

class LnStub(table: Map<Double, Double>, tolerance: Double = 1e-9) :
    FunctionModule by TableFunctionStub(table, tolerance)

class Log2Stub(table: Map<Double, Double>, tolerance: Double = 1e-9) :
    FunctionModule by TableFunctionStub(table, tolerance)

class Log5Stub(table: Map<Double, Double>, tolerance: Double = 1e-9) :
    FunctionModule by TableFunctionStub(table, tolerance)

class SystemStub(table: Map<Double, Double>, tolerance: Double = 1e-9) :
    FunctionModule by TableFunctionStub(table, tolerance)
