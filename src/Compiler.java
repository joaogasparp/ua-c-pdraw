import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.*;

@SuppressWarnings("CheckReturnValue")
public class Compiler extends pdrawGrammarBaseVisitor<ST> {

   STGroup templates = new STGroupFile("templates.stg");
   HashMap<String, String> dictionary = new HashMap<>();
   Map<String, String> pens = new HashMap<>();

   @Override
   public ST visitProgram(pdrawGrammarParser.ProgramContext ctx) {
      ST res = templates.getInstanceOf("main");

      List<ST> penDefinitions = new ArrayList<>();
      List<ST> instructions = new ArrayList<>();
      List<ST> variableAssignment = new ArrayList<>();
      String penName = null;

      for (pdrawGrammarParser.StatContext stat : ctx.stat()) {
         if (stat.penDefinition() != null) {
            penDefinitions.add(visit(stat.penDefinition()));
         } else if (stat.variableAssignment() != null) {
            variableAssignment.add(visit(stat.variableAssignment()));
         } else {
            instructions.add(visit(stat.expr()));
         }
      }

      res.add("variableAssignment", variableAssignment);
      res.add("penDefinitions", penDefinitions);
      res.add("instructions", instructions);

      return res;
   }

   @Override
   public ST visitStat(pdrawGrammarParser.StatContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitIfStatement(pdrawGrammarParser.IfStatementContext ctx) {
      ST res = templates.getInstanceOf("ifStatement");
      res.add("value", visit(ctx.value()));
      List<ST> stats = new ArrayList<>();
      for (pdrawGrammarParser.StatContext stat : ctx.stat()) {
         stats.add(visit(stat));
      }
      res.add("stats", stats);
      return res;
   }

   @Override
   public ST visitLoopStatement(pdrawGrammarParser.LoopStatementContext ctx) {
      ST res = templates.getInstanceOf("loopStatement");
      res.add("value", visit(ctx.value()));
      List<ST> stats = new ArrayList<>();
      for (pdrawGrammarParser.StatContext stat : ctx.stat()) {
         stats.add(visit(stat));
      }
      res.add("stats", stats);
      return res;
   }

   @Override
   public ST visitForStatement(pdrawGrammarParser.ForStatementContext ctx) {
      ST res = templates.getInstanceOf("forStatement");
      res.add("init", visit(ctx.expr(0)));
      res.add("condition", visit(ctx.value()));
      res.add("update", visit(ctx.expr(1)));
      List<ST> stats = new ArrayList<>();
      for (pdrawGrammarParser.StatContext stat : ctx.stat()) {
         stats.add(visit(stat));
      }
      res.add("stats", stats);
      return res;
   }

   @Override
   public ST visitExpr(pdrawGrammarParser.ExprContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitPenDefinition(pdrawGrammarParser.PenDefinitionContext ctx) {
      ST res = templates.getInstanceOf("penDefinition");
      String penName = ctx.ID().getText();
      res.add("penName", penName);

      for (pdrawGrammarParser.PenAttributeContext attr : ctx.penAttribute()) {
         String attributeName = attr.attribute().getText();
         res.add("attributes", visit(attr));

         switch (attributeName) {
            case "color":
               res.add("applyInstructions", "t.color(self.color)");
               res.add("strInstructions", "f'Color: {self.color}'");
               break;
            case "position":
               res.add("applyInstructions", "t.penup()\nt.goto(self.position)");
               res.add("strInstructions", "f'Position: {self.position}'");
               break;
            case "orientation":
               res.add("applyInstructions", "t.setheading(self.orientation)");
               res.add("strInstructions", "f'Orientation: {self.orientation}ยบ'");
               break;
            case "thickness":
               res.add("applyInstructions", "t.pensize(self.thickness)");
               res.add("strInstructions", "f'Thickness: {self.thickness}'");
               break;
            case "pressure":
               double pressure = Double.parseDouble(attr.value().getText());
               if (pressure >= 0 && pressure <= 1) {
                  res.add("applyInstructions", "t.pendown()");
               } else if (pressure == -1) {
                  res.add("applyInstructions", "t.penup()");
               }
               res.add("strInstructions", "f'Pressure: {self.pressure}'");
               break;
         }
      }

      return res;
   }

   @Override
   public ST visitPenAttribute(pdrawGrammarParser.PenAttributeContext ctx) {
      ST res = templates.getInstanceOf("penAttribute");
      String attributeName = ctx.attribute().getText();
      String attributeValue = ctx.value().getText();

      switch (attributeName) {
         case "color":
            attributeValue = "\"" + attributeValue + "\"";
            break;
         case "position":
            if (attributeValue.contains("(") && attributeValue.contains(")")) {
               if (attributeValue.contains("//")) {
                  attributeValue = attributeValue.replace("//", "/");
               }
               if (attributeValue.contains("\\\\")) {
                  attributeValue = attributeValue.replace("\\\\", "%");
               }
            }
            break;
         case "orientation":
            if (attributeValue.contains("º")) {
               attributeValue = attributeValue.substring(0, attributeValue.length() - 1);
            }
            break;
         case "thickness":
            break;
         case "pressure":
            break;
      }

      res.add("attributeName", attributeName);
      res.add("attributeValue", attributeValue);

      return res;
   }

   @Override
   public ST visitPenCreation(pdrawGrammarParser.PenCreationContext ctx) {
      ST res = templates.getInstanceOf("penCreation");

      String penName = ctx.ID(0).getText();
      res.add("penName", penName);

      String penNameTurtle = penName + "Turtle";
      res.add("penNameTurtle", penNameTurtle);

      if (ctx.ID().size() == 2) {
         String penDefinition = ctx.ID(1).getText();
         res.add("penDefinition", penDefinition + "()");
      } else if (ctx.ID().size() == 1) {
         res.add("penDefinition", "Pen(\"\", (0, 0), 0, 0, 0)");
      }

      pens.put(penName, penNameTurtle);

      return res;
   }

   @Override
   public ST visitPenAction(pdrawGrammarParser.PenActionContext ctx) {
      ST res = templates.getInstanceOf("penAction");
      String penName = ctx.ID().getText();
      res.add("penName", penName);

      List<ST> commands = new ArrayList<>();
      for (pdrawGrammarParser.PenCommandContext command : ctx.penCommand()) {
         commands.add(visitPenCommand(command, penName));
      }

      res.add("commands", commands);
      return res;
   }

   public ST visitPenCommand(pdrawGrammarParser.PenCommandContext ctx, String penName) {
      if (ctx instanceof pdrawGrammarParser.PenverticalContext) {
         return visitPenvertical((pdrawGrammarParser.PenverticalContext) ctx, penName);
      } else if (ctx instanceof pdrawGrammarParser.PenMovementContext) {
         return visitPenMovement((pdrawGrammarParser.PenMovementContext) ctx, penName);
      } else if (ctx instanceof pdrawGrammarParser.PenRotateContext) {
         return visitPenRotate((pdrawGrammarParser.PenRotateContext) ctx, penName);
      } else {
         throw new IllegalArgumentException("Unknown command type: " + ctx);
      }
   }

   public ST visitPenvertical(pdrawGrammarParser.PenverticalContext ctx, String penName) {
      ST res = templates.getInstanceOf("penvertical");
      String direction = ctx.vertical().getText();

      if (direction.equals("up")) {
         res.add("direction", "penup");
      } else {
         res.add("direction", "pendown");
      }

      res.add("penName", penName);

      return res;
   }

   public ST visitPenMovement(pdrawGrammarParser.PenMovementContext ctx, String penName) {
      ST res = templates.getInstanceOf("penMovement");
      String direction = ctx.movement().getText();
      String value = ctx.value().getText();

      if (direction.equals("forward")) {
         res.add("direction", "forward");
      } else {
         res.add("direction", "backward");
      }

      res.add("penName", penName);
      res.add("value", value);

      return res;
   }

   public ST visitPenRotate(pdrawGrammarParser.PenRotateContext ctx, String penName) {
      ST res = templates.getInstanceOf("penRotate");
      String direction = ctx.rotate().getText();
      String value = ctx.value().getText();

      if (direction.equals("right")) {
         res.add("direction", "right");
      } else {
         res.add("direction", "left");
      }

      if (value.contains("º")) {
         value = value.substring(0, value.length() - 1);
      }

      res.add("penName", penName);
      res.add("value", value);

      return res;
   }

   @Override
   public ST visitPauseCommand(pdrawGrammarParser.PauseCommandContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitVertical(pdrawGrammarParser.VerticalContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitMovement(pdrawGrammarParser.MovementContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitRotate(pdrawGrammarParser.RotateContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitPenValueAtribution(pdrawGrammarParser.PenValueAtributionContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitAttribute(pdrawGrammarParser.AttributeContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitDefineCanvas(pdrawGrammarParser.DefineCanvasContext ctx) {
      ST res = templates.getInstanceOf("defineCanvas");
      res.add("id", ctx.ID().getText());
      res.add("string", ctx.STRING().getText());
      if (ctx.value() != null && ctx.value().size() == 2) {
         res.add("width", visit(ctx.value(0)));
         res.add("height", visit(ctx.value(1)));
      }
      return res;
   }

   @Override
   public ST visitExecuteProgram(pdrawGrammarParser.ExecuteProgramContext ctx) {
      ST res = templates.getInstanceOf("executeProgram");
      res.add("penName", ctx.ID().getText());
      res.add("fileName", ctx.STRING().getText());
      return res;
   }

   @Override
   public ST visitVariableAssignment(pdrawGrammarParser.VariableAssignmentContext ctx) {
      ST res = templates.getInstanceOf("variableAssignment");
      List<ST> assignments = new ArrayList<>();
      for (pdrawGrammarParser.AssignmentExpressionContext expr : ctx.assignmentExpression()) {
         assignments.add(visit(expr));
      }
      res.add("assignments", assignments);
      return res;
   }

   @Override
   public ST visitAssignmentExpression(pdrawGrammarParser.AssignmentExpressionContext ctx) {
      ST res = templates.getInstanceOf("assignmentExpression");
      res.add("id", ctx.ID().getText());
      res.add("value", visit(ctx.attributionExpression()));
      return res;
   }

   @Override
   public ST visitAttributionExpression(pdrawGrammarParser.AttributionExpressionContext ctx) {
      ST res = templates.getInstanceOf("attributionExpression");
      if (ctx.value() != null) {
         res.add("value", visit(ctx.value()));
      } else if (ctx.typeConversion() != null) {
         res.add("typeConversion", visit(ctx.typeConversion()));
      }
      return res;
   }

   @Override
   public ST visitReadStatement(pdrawGrammarParser.ReadStatementContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitWriteStatement(pdrawGrammarParser.WriteStatementContext ctx) {
      ST res = templates.getInstanceOf("writeStatement");
      String value = ctx.value().getText();

      if (pens.containsKey(value)) {
         String pen = pens.get(value);
         res.add("value", pen);
      } else {
         res.add("value", value);
      }

      return res;
   }

   @Override
   public ST visitPauseStatement(pdrawGrammarParser.PauseStatementContext ctx) {
      ST res = templates.getInstanceOf("pauseStatement");
      long valueInMicroseconds = Long.parseLong(ctx.NUMERIC().getText());
      double valueInSeconds = valueInMicroseconds / 1000000.0;
      res.add("value", valueInSeconds);
      return res;
   }

   @Override
   public ST visitCondition(pdrawGrammarParser.ConditionContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitPonto(pdrawGrammarParser.PontoContext ctx) {
      ST res = templates.getInstanceOf("valuePonto");
      for (int i = 0; i < 2; i++) {
         ST value = visit(ctx.value(i));
         res.add("value" + (i + 1), value);
      }
      return res;
   }

   @Override
   public ST visitColorName(pdrawGrammarParser.ColorNameContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitColorHex(pdrawGrammarParser.ColorHexContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitTypeConversion(pdrawGrammarParser.TypeConversionContext ctx) {
      ST res = templates.getInstanceOf("typeConversion");
      String type = visit(ctx.type()).render();
      if (type.equals("real")) {
         type = "float";
      } else if (type.equals("int")) {
         type = "int";
      } else if (type.equals("string")) {
         type = "str";
      } else if (type.equals("bool")) {
         type = "bool";
      }
      res.add("type", type);
      res.add("value", visit(ctx.value()));
      return res;
   }

   @Override
   public ST visitType(pdrawGrammarParser.TypeContext ctx) {
      ST res = templates.getInstanceOf("type");
      res.add("typeName", ctx.getText());
      return res;
   }

   @Override
   public ST visitInputStatement(pdrawGrammarParser.InputStatementContext ctx) {
      ST res = templates.getInstanceOf("inputStatement");
      res.add("prompt", ctx.STRING().getText());
      return res;
   }

   @Override
   public ST visitValueID(pdrawGrammarParser.ValueIDContext ctx) {
      ST res = templates.getInstanceOf("valueID");
      String id = ctx.ID().getText();
      res.add("id", id);
      return res;
   }

   @Override
   public ST visitValueExprSomDiv(pdrawGrammarParser.ValueExprSomDivContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitValueTypeConvert(pdrawGrammarParser.ValueTypeConvertContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitValueCondSymb(pdrawGrammarParser.ValueCondSymbContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitValueDegree(pdrawGrammarParser.ValueDegreeContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitValueExprAritmetica(pdrawGrammarParser.ValueExprAritmeticaContext ctx) {
      ST res = templates.getInstanceOf("valueExprAritmetica");
      ST value1 = visit(ctx.value(0));
      ST value2 = visit(ctx.value(1));

      String operator = ctx.op.getText();

      switch (operator) {
         case "*":
            res.add("operator", "multiply");
            break;
         case "/":
            res.add("operator", "divide");
            break;
         case "//":
            res.add("operator", "divideInteira");
            break;
         case "\\\\":
            res.add("operator", "rest");
            break;
      }

      res.add("value1", value1);
      res.add("value2", value2);

      return res;
   }

   @Override
   public ST visitValueExprParentises(pdrawGrammarParser.ValueExprParentisesContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitValueNum(pdrawGrammarParser.ValueNumContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitValueInput(pdrawGrammarParser.ValueInputContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitValueCondAndOr(pdrawGrammarParser.ValueCondAndOrContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitValueColor(pdrawGrammarParser.ValueColorContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitValueBool(pdrawGrammarParser.ValueBoolContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitValueString(pdrawGrammarParser.ValueStringContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitValuePonto(pdrawGrammarParser.ValuePontoContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }

   @Override
   public ST visitBoolean(pdrawGrammarParser.BooleanContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      // return res;
   }
}
