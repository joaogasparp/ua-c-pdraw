from antlr4 import *
from ipdrawGrammarParser import ipdrawGrammarParser
from ipdrawGrammarVisitor import ipdrawGrammarVisitor

class ipdrawGrammar(ipdrawGrammarVisitor):  
   
   def __init__(self):
      self.penName = None
   
   def visitProgram(self, ctx:ipdrawGrammarParser.ProgramContext):
      return self.visitChildren(ctx)

   def visitStat(self, ctx:ipdrawGrammarParser.StatContext):
      return self.visitChildren(ctx)

   def visitIfStatement(self, ctx:ipdrawGrammarParser.IfStatementContext):
      return self.visitChildren(ctx)

   def visitLoopStatement(self, ctx:ipdrawGrammarParser.LoopStatementContext):
      return self.visitChildren(ctx)

   def visitForStatement(self, ctx:ipdrawGrammarParser.ForStatementContext):
      return self.visitChildren(ctx)

   def visitExpr(self, ctx:ipdrawGrammarParser.ExprContext):
      return self.visitChildren(ctx)

   def visitPenAttribute(self, ctx:ipdrawGrammarParser.PenAttributeContext):
      return self.visitChildren(ctx)

   def visitPenAction(self, ctx:ipdrawGrammarParser.PenActionContext):
      return self.visitChildren(ctx)

   def visitPenvertical(self, ctx:ipdrawGrammarParser.PenverticalContext):
      return self.visitChildren(ctx)

   def visitPenMovement(self, ctx:ipdrawGrammarParser.PenMovementContext):
      return self.visitChildren(ctx)

   def visitPenRotate(self, ctx:ipdrawGrammarParser.PenRotateContext):
      return self.visitChildren(ctx)

   def visitPauseCommand(self, ctx:ipdrawGrammarParser.PauseCommandContext):
      return self.visitChildren(ctx)

   def visitVertical(self, ctx:ipdrawGrammarParser.VerticalContext):
      return self.visitChildren(ctx)

   def visitMovement(self, ctx:ipdrawGrammarParser.MovementContext):
      return self.visitChildren(ctx)

   def visitRotate(self, ctx:ipdrawGrammarParser.RotateContext):
      return self.visitChildren(ctx)

   def visitAttribute(self, ctx:ipdrawGrammarParser.AttributeContext):
      return self.visitChildren(ctx)

   def visitVariableAssignment(self, ctx:ipdrawGrammarParser.VariableAssignmentContext):
      return self.visitChildren(ctx)

   def visitAssignmentExpression(self, ctx:ipdrawGrammarParser.AssignmentExpressionContext):
      return self.visitChildren(ctx)

   def visitAttributionExpression(self, ctx:ipdrawGrammarParser.AttributionExpressionContext):
      return self.visitChildren(ctx)

   def visitReadStatement(self, ctx:ipdrawGrammarParser.ReadStatementContext):
      return self.visitChildren(ctx)

   def visitWriteStatement(self, ctx:ipdrawGrammarParser.WriteStatementContext):
      return self.visitChildren(ctx)

   def visitPauseStatement(self, ctx:ipdrawGrammarParser.PauseStatementContext):
      return self.visitChildren(ctx)

   def visitCondition(self, ctx:ipdrawGrammarParser.ConditionContext):
      return self.visitChildren(ctx)

   def visitPonto(self, ctx:ipdrawGrammarParser.PontoContext):
      return self.visitChildren(ctx)

   def visitColorName(self, ctx:ipdrawGrammarParser.ColorNameContext):
      return self.visitChildren(ctx)

   def visitColorHex(self, ctx:ipdrawGrammarParser.ColorHexContext):
      return self.visitChildren(ctx)

   def visitTypeConversion(self, ctx:ipdrawGrammarParser.TypeConversionContext):
      return self.visitChildren(ctx)

   def visitType(self, ctx:ipdrawGrammarParser.TypeContext):
      return self.visitChildren(ctx)

   def visitInputStatement(self, ctx:ipdrawGrammarParser.InputStatementContext):
      return self.visitChildren(ctx)

   def visitValueID(self, ctx:ipdrawGrammarParser.ValueIDContext):
      return self.visitChildren(ctx)

   def visitValueExprSomDiv(self, ctx:ipdrawGrammarParser.ValueExprSomDivContext):
      return self.visitChildren(ctx)

   def visitValueTypeConvert(self, ctx:ipdrawGrammarParser.ValueTypeConvertContext):
      return self.visitChildren(ctx)

   def visitValueCondSymb(self, ctx:ipdrawGrammarParser.ValueCondSymbContext):
      return self.visitChildren(ctx)

   def visitValueDegree(self, ctx:ipdrawGrammarParser.ValueDegreeContext):
      return self.visitChildren(ctx)

   def visitValueExprAritmetica(self, ctx:ipdrawGrammarParser.ValueExprAritmeticaContext):
      return self.visitChildren(ctx)

   def visitValueExprParentises(self, ctx:ipdrawGrammarParser.ValueExprParentisesContext):
      return self.visitChildren(ctx)

   def visitValueNum(self, ctx:ipdrawGrammarParser.ValueNumContext):
      return self.visitChildren(ctx)

   def visitValueInput(self, ctx:ipdrawGrammarParser.ValueInputContext):
      return self.visitChildren(ctx)

   def visitValueCondAndOr(self, ctx:ipdrawGrammarParser.ValueCondAndOrContext):
      return self.visitChildren(ctx)

   def visitValueColor(self, ctx:ipdrawGrammarParser.ValueColorContext):
      return self.visitChildren(ctx)

   def visitValueBool(self, ctx:ipdrawGrammarParser.ValueBoolContext):
      return self.visitChildren(ctx)

   def visitValueString(self, ctx:ipdrawGrammarParser.ValueStringContext):
      return self.visitChildren(ctx)

   def visitValuePonto(self, ctx:ipdrawGrammarParser.ValuePontoContext):
      return self.visitChildren(ctx)

   def visitBoolean(self, ctx:ipdrawGrammarParser.BooleanContext):
      return self.visitChildren(ctx)

