import arcpy
import math
import os


class HexGrid:
    def __init__(self, size=100, orig_x=0.0, orig_y=0.0):
        self.orig_x = orig_x
        self.orig_y = orig_y
        self.size = size
        self.h = self.size * math.cos(30.0 * math.pi / 180.0)
        self.v = self.size * 0.5
        self.skip_x = 2.0 * self.h
        self.skip_y = 3.0 * self.v

    def rc2xy(self, r, c):
        ofs = self.h if r % 2 != 0 else 0
        x = c * self.skip_x + ofs + self.orig_x
        y = r * self.skip_y + self.orig_y
        return x, y


class HexCell:
    def __init__(self, size=100):
        self.xy = []
        for i in range(7):
            angle = math.pi * ((i % 6) + 0.5) / 3.0
            x = size * math.cos(angle)
            y = size * math.sin(angle)
            self.xy.append((x, y))

    def toShape(self, cx, cy):
        return [[cx + x, cy + y] for (x, y) in self.xy]


class Toolbox(object):
    def __init__(self):
        self.label = "Toolbox"
        self.alias = "Toolbox"
        self.tools = [Tool]


class Tool(object):
    def __init__(self):
        self.label = "HexTool"
        self.description = "HexTool"
        self.canRunInBackground = False

    def getParameterInfo(self):
        param_fc_pnt = arcpy.Parameter(
            name="outputPoint",
            displayName="outputPoint",
            direction="Output",
            datatype="Feature Layer",
            parameterType="Derived")

        param_fc_hex = arcpy.Parameter(
            name="outputPoly",
            displayName="outputPoly",
            direction="Output",
            datatype="Feature Layer",
            parameterType="Derived")

        param_x = arcpy.Parameter(
            name="param_x",
            displayName="X",
            direction="Input",
            datatype="GPDouble",
            parameterType="Required")
        param_x.value = -7859566.073085553

        param_y = arcpy.Parameter(
            name="param_y",
            displayName="X",
            direction="Input",
            datatype="GPDouble",
            parameterType="Required")
        param_y.value = 5097327.411990407

        param_row = arcpy.Parameter(
            name="param_row",
            displayName="Row",
            direction="Input",
            datatype="GPDouble",
            parameterType="Required")
        param_row.value = 67965

        param_col = arcpy.Parameter(
            name="param_col",
            displayName="Col",
            direction="Input",
            datatype="GPDouble",
            parameterType="Required")
        param_col.value = -90755

        param_size = arcpy.Parameter(
            name="param_size",
            displayName="Size",
            direction="Input",
            datatype="GPDouble",
            parameterType="Required")
        param_size.value = 50.0

        return [param_fc_pnt,
                param_fc_hex,
                param_x, param_y,
                param_row, param_col, param_size
                ]

    def isLicensed(self):
        return True

    def updateParameters(self, parameters):
        return

    def updateMessages(self, parameters):
        return

    def executePnt(self, parameters):
        x = parameters[2].value
        y = parameters[3].value

        in_memory = True

        name = "Point"
        if in_memory:
            ws = "in_memory"
            fc = ws + "/" + name
        else:
            fc = os.path.join(arcpy.env.scratchGDB, name)
            ws = os.path.dirname(fc)
        if arcpy.Exists(fc):
            arcpy.management.Delete(fc)

        sp_ref = arcpy.SpatialReference(102100)
        arcpy.management.CreateFeatureclass(ws, name, "POINT", spatial_reference=sp_ref)
        with arcpy.da.InsertCursor(fc, ['SHAPE@XY']) as cursor:
            cursor.insertRow([(x, y)])

        parameters[0].value = fc
        return

    def executeHex(self, parameters):
        r = parameters[4].value
        c = parameters[5].value
        size = parameters[6].value

        hex_cell = HexCell(size=size)
        hex_grid = HexGrid(size=size)

        in_memory = True
        name = "Hex"
        if in_memory:
            ws = "in_memory"
            fc = ws + "/" + name
        else:
            fc = os.path.join(arcpy.env.scratchGDB, name)
            ws = os.path.dirname(fc)
        if arcpy.Exists(fc):
            arcpy.management.Delete(fc)

        sp_ref = arcpy.SpatialReference(102100)
        arcpy.management.CreateFeatureclass(ws, name, "POLYGON", spatial_reference=sp_ref)
        with arcpy.da.InsertCursor(fc, ['SHAPE@']) as cursor:
            x, y = hex_grid.rc2xy(r, c)
            cursor.insertRow([hex_cell.toShape(x, y)])

        parameters[1].value = fc
        return

    def execute(self, parameters, messages):
        self.executePnt(parameters)
        self.executeHex(parameters)
        return
