import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.stringtemplate.v4.*;

@SuppressWarnings("CheckReturnValue")
public class iCompiler extends ipdrawGrammarBaseVisitor<ST> {

   STGroup templates = new STGroupFile("templates.stg");
   HashMap<String, String> dictionary = new HashMap<>();

   @Override
   public ST visitProgram(ipdrawGrammarParser.ProgramContext ctx) {
      ST res = templates.getInstanceOf("main");

      List<ST> variableAssignments = new ArrayList<>();
      List<ST> instructions = new ArrayList<>();

      for (ipdrawGrammarParser.StatContext stat : ctx.stat()) {
         if (stat.expr() != null) {
               ST exprTemplate = visit(stat.expr());
               if (stat.expr().variableAssignment() != null) {
                  variableAssignments.add(exprTemplate);
               } else {
                  instructions.add(exprTemplate);
               }
         } else if (stat.ifStatement() != null) {
               instructions.add(visit(stat.ifStatement()));
         } else if (stat.loopStatement() != null) {
               instructions.add(visit(stat.loopStatement()));
         } else if (stat.forStatement() != null) {
               instructions.add(visit(stat.forStatement()));
         }
      }

      res.add("variableAssignment", variableAssignments);
      res.add("instructions", instructions);

      return res;
   }

 

   @Override public ST visitStat(ipdrawGrammarParser.StatContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitIfStatement(ipdrawGrammarParser.IfStatementContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitLoopStatement(ipdrawGrammarParser.LoopStatementContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitForStatement(ipdrawGrammarParser.ForStatementContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitExpr(ipdrawGrammarParser.ExprContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitPenAttribute(ipdrawGrammarParser.PenAttributeContext ctx) {
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

   @Override public ST visitPenAction(ipdrawGrammarParser.PenActionContext ctx) {
      ST res = templates.getInstanceOf("penAction");

      List<ST> commands = new ArrayList<>();
      for (ipdrawGrammarParser.PenCommandContext command : ctx.penCommand()) {
         commands.add(visitPenCommand(command));
      }

      res.add("commands", commands);
      return res;
   }

   public ST visitPenCommand(ipdrawGrammarParser.PenCommandContext ctx) {
      if (ctx instanceof ipdrawGrammarParser.PenverticalContext) {
         return visitPenvertical((ipdrawGrammarParser.PenverticalContext) ctx);
      } else if (ctx instanceof ipdrawGrammarParser.PenMovementContext) {
         return visitPenMovement((ipdrawGrammarParser.PenMovementContext) ctx);
      } else if (ctx instanceof ipdrawGrammarParser.PenRotateContext) {
         return visitPenRotate((ipdrawGrammarParser.PenRotateContext) ctx);
      } else {
         throw new IllegalArgumentException("Unknown command type: " + ctx);
      }
   }

   @Override public ST visitPenvertical(ipdrawGrammarParser.PenverticalContext ctx) {
      ST res = templates.getInstanceOf("penvertical");
      String direction = ctx.vertical().getText();

      if (direction.equals("up")) {
         res.add("direction", "penup");
      } else {
         res.add("direction", "pendown");
      }

      return res;
   }

   @Override public ST visitPenMovement(ipdrawGrammarParser.PenMovementContext ctx) {
      ST res = templates.getInstanceOf("penMovement");
      String direction = ctx.movement().getText();
      String value = ctx.value().getText();

      if (direction.equals("forward")) {
         res.add("direction", "forward");
      } else {
         res.add("direction", "backward");
      }

      res.add("value", value);

      return res;
   }

   @Override public ST visitPenRotate(ipdrawGrammarParser.PenRotateContext ctx) {
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

      res.add("value", value);

      return res;
   }

   @Override public ST visitPauseCommand(ipdrawGrammarParser.PauseCommandContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitVertical(ipdrawGrammarParser.VerticalContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitMovement(ipdrawGrammarParser.MovementContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitRotate(ipdrawGrammarParser.RotateContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitAttribute(ipdrawGrammarParser.AttributeContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitVariableAssignment(ipdrawGrammarParser.VariableAssignmentContext ctx) {
      ST res = templates.getInstanceOf("variableAssignment");
      List<ST> assignments = new ArrayList<>();
      for (ipdrawGrammarParser.AssignmentExpressionContext expr : ctx.assignmentExpression()) {
         assignments.add(visit(expr));
      }
      res.add("assignments", assignments);
      return res;
   }

   @Override public ST visitAssignmentExpression(ipdrawGrammarParser.AssignmentExpressionContext ctx) {
      ST res = templates.getInstanceOf("assignmentExpression");
      res.add("id", ctx.ID().getText());
      res.add("value", visit(ctx.attributionExpression()));
      return res;
   }

   @Override public ST visitAttributionExpression(ipdrawGrammarParser.AttributionExpressionContext ctx) {
      ST res = templates.getInstanceOf("attributionExpression");
      if (ctx.value() != null) {
         res.add("value", visit(ctx.value()));
      } else if (ctx.typeConversion() != null) {
         res.add("typeConversion", visit(ctx.typeConversion()));
      }
      return res;
   }

   @Override public ST visitReadStatement(ipdrawGrammarParser.ReadStatementContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitWriteStatement(ipdrawGrammarParser.WriteStatementContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitPauseStatement(ipdrawGrammarParser.PauseStatementContext ctx) {
      ST res = templates.getInstanceOf("pauseStatement");
      long valueInMicroseconds = Long.parseLong(ctx.NUMERIC().getText());
      double valueInSeconds = valueInMicroseconds / 1000000.0;
      res.add("value", valueInSeconds);
      return res;
   }

   @Override public ST visitCondition(ipdrawGrammarParser.ConditionContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitPonto(ipdrawGrammarParser.PontoContext ctx) {
      ST res = templates.getInstanceOf("valuePonto");
      for (int i = 0; i < 2; i++) {
         ST value = visit(ctx.value(i));
         res.add("value" + (i + 1), value);
      }
      return res;
   }

   @Override public ST visitColorName(ipdrawGrammarParser.ColorNameContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitColorHex(ipdrawGrammarParser.ColorHexContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitTypeConversion(ipdrawGrammarParser.TypeConversionContext ctx) {
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

   @Override public ST visitType(ipdrawGrammarParser.TypeContext ctx) {
      ST res = templates.getInstanceOf("type");
      res.add("typeName", ctx.getText());
      return res;
   }

   @Override public ST visitInputStatement(ipdrawGrammarParser.InputStatementContext ctx) {
      ST res = templates.getInstanceOf("inputStatement");
      res.add("prompt", ctx.STRING().getText());
      return res;
   }

   @Override public ST visitValueID(ipdrawGrammarParser.ValueIDContext ctx) {
      ST res = templates.getInstanceOf("valueID");
      String id = ctx.ID().getText();
      res.add("id", dictionary.get(id));
      return res;
   }

   @Override public ST visitValueExprSomDiv(ipdrawGrammarParser.ValueExprSomDivContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitValueTypeConvert(ipdrawGrammarParser.ValueTypeConvertContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitValueCondSymb(ipdrawGrammarParser.ValueCondSymbContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitValueDegree(ipdrawGrammarParser.ValueDegreeContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitValueExprAritmetica(ipdrawGrammarParser.ValueExprAritmeticaContext ctx) {
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

   @Override public ST visitValueExprParentises(ipdrawGrammarParser.ValueExprParentisesContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitValueNum(ipdrawGrammarParser.ValueNumContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitValueInput(ipdrawGrammarParser.ValueInputContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitValueCondAndOr(ipdrawGrammarParser.ValueCondAndOrContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitValueColor(ipdrawGrammarParser.ValueColorContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitValueBool(ipdrawGrammarParser.ValueBoolContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitValueString(ipdrawGrammarParser.ValueStringContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitValuePonto(ipdrawGrammarParser.ValuePontoContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }

   @Override public ST visitBoolean(ipdrawGrammarParser.BooleanContext ctx) {
      ST res = null;
      return visitChildren(ctx);
      //return res;
   }
}
