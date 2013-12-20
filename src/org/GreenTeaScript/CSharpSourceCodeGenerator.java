package org.GreenTeaScript;

import java.util.ArrayList;

public class CSharpSourceCodeGenerator extends GtSourceGenerator {

	public CSharpSourceCodeGenerator(String TargetCode, String OutputFile, int GeneratorFlag) {
		super(TargetCode, OutputFile, GeneratorFlag);
	}
	
	@Override
	public void GenerateFunc(GtFunc Func, ArrayList<String> ParamNameList, GtNode Body) {
		String MethodName = Func.GetNativeFuncName();
		GtSourceBuilder Builder = new GtSourceBuilder(this);
		Builder.Append("class");
		Builder.SpaceAppendSpace(HolderClassName(MethodName));
		Builder.AppendLine("{");
		Builder.Indent();
		Builder.IndentAndAppend("static ");
		Builder.Append(Func.Types[0].ShortName.toString());
		Builder.SpaceAppendSpace(MethodName);
		Builder.Append("(");

		Builder.Append(")");
		GtSourceBuilder PushedBuilder = this.VisitingBuilder;
		this.VisitingBuilder = Builder;
		this.VisitIndentBlock("{", Body, "}");
		this.VisitingBuilder = PushedBuilder;
		Builder.AppendLine("");
		Builder.UnIndent();
		Builder.AppendLine("}");
		System.out.println(Builder);
	}
	
	@Override
	public void VisitReturnNode(GtReturnNode Node) {
		this.VisitingBuilder.Append("return");
		if(this.DoesNodeExist(Node.Expr)){
			this.VisitingBuilder.Append(" ");
			Node.Expr.Evaluate(this);
		}
	}
	
//	private String GetLocalType(GtType Type, boolean IsPointer) {
//		if(Type.IsDynamicType() || Type.IsNativeType()) {
//			if(Type.IsBooleanType()) {
//				return "int";
//			}
//			return Type.ShortName;
//		}
//		/*local*/String TypeName = "struct " + Type.ShortName;
//		if(IsPointer) {
//			TypeName += "*";
//		}
//		return TypeName;
//
//	}
//	public String NativeTypeName(GtType Type) {
//		return this.GetLocalType(Type, false);
//	}
//
//	public String LocalTypeName(GtType Type) {
//		return this.GetLocalType(Type, true);
//	}
//
//	public String GtTypeName(GtType Type) {
//		return Type.ShortName;
//	}
	
	String HolderClassName(String NativeName) {
		return "Func" + NativeName;
	}
	@Override public void OpenClassField(GtSyntaxTree ParsedTree, GtType ClassType, GtClassField ClassField) {
	}

	@Override public void VisitConstNode(GtConstNode Node) {
		this.VisitingBuilder.Append(Node.Token.ParsedText);
	}

	@Override public void VisitNewNode(GtNewNode Node) {
	}
	
	@Override public void VisitNullNode(GtNullNode Node) {
		this.VisitingBuilder.Append(this.NullLiteral);
	}

	@Override public void VisitLocalNode(GtLocalNode Node) {
		this.VisitingBuilder.Append(Node.NativeName);
	}
	
	@Override public void VisitConstructorNode(GtConstructorNode Node) {
	}
	
	@Override public void VisitGetterNode(GtGetterNode Node) {
		Node.RecvNode.Evaluate(this);
		this.VisitingBuilder.Append(".");
		this.VisitingBuilder.Append(Node.Func.FuncName);
	}
	
	@Override public void VisitSetterNode(GtSetterNode Node) {
		Node.RecvNode.Evaluate(this);
		this.VisitingBuilder.Append(".");
		this.VisitingBuilder.Append(Node.Func.FuncName);
		this.VisitingBuilder.Append("=");
		Node.ValueNode.Evaluate(this);
	}
	@Override public void VisitApplyNode(GtApplyNode Node) {
	}

	@Override public void VisitStaticApplyNode(GtStaticApplyNode Node) {
		this.VisitingBuilder.Append(Node.Func.GetNativeFuncName());
		this.VisitingBuilder.Append("(");
		for(/*local*/int i = 0; i < LibGreenTea.ListSize(Node.ParamList); i++){
			Node.ParamList.get(i).Evaluate(this);
		}
		this.VisitingBuilder.Append(")");
	}
	
	@Override public void VisitBinaryNode(GtBinaryNode Node) {
		//if(Node.Func.FuncBody instanceof Method) {
		//this.VisitingBuilder.Append("(");
		Node.LeftNode.Evaluate(this);
		this.VisitingBuilder.SpaceAppendSpace(Node.Token.ParsedText);
		Node.RightNode.Evaluate(this);
		//this.VisitingBuilder.Append(")");
		//}
	}

	@Override public void VisitUnaryNode(GtUnaryNode Node) {
//		if(Node.Func.FuncBody instanceof Method) {
		this.VisitingBuilder.Append("(");
		this.VisitingBuilder.Append(Node.Token.ParsedText);
		Node.Expr.Evaluate(this);
		this.VisitingBuilder.Append(")");
//		}
	}
	
	@Override public void VisitIndexerNode(GtIndexerNode Node) {
	}

	@Override public void VisitArrayNode(GtArrayNode Node) {
	}

	public void VisitNewArrayNode(GtNewArrayNode Node) {
	}
	
	@Override public void VisitAndNode(GtAndNode Node) {
		this.VisitingBuilder.Append("(");
		Node.LeftNode.Evaluate(this);
		this.VisitingBuilder.Append(" && ");
		Node.RightNode.Evaluate(this);
		this.VisitingBuilder.Append(")");
	}

	@Override public void VisitOrNode(GtOrNode Node) {
		this.VisitingBuilder.Append("(");
		Node.LeftNode.Evaluate(this);
		this.VisitingBuilder.Append(" || ");
		Node.RightNode.Evaluate(this);
		this.VisitingBuilder.Append(")");
	}
	@Override public void VisitAssignNode(GtAssignNode Node) {
	}

	@Override public void VisitSelfAssignNode(GtSelfAssignNode Node) {
	}
	public void VisitVarNode(GtVarNode Node) {
		//this.VisitingBuilder.Append(this.ConvertToNativeTypeName(Node.DeclType));
		this.VisitingBuilder.Append(Node.DeclType.toString());
		String VarName = Node.Token.ParsedText;
		this.VisitingBuilder.SpaceAppendSpace(VarName);
		//if(this.DoesNodeExist(Node.InitNode)){ //FIXME: Always true
		if(Node.InitNode.Token.ParsedText != VarName){
			this.VisitingBuilder.SpaceAppendSpace("=");
			Node.InitNode.Evaluate(this);
		}
		this.VisitingBuilder.AppendLine(";");
		this.VisitBlockWithoutIndent(Node.BlockNode);
	}
	
	private void VisitBlockWithoutIndent(GtNode Node) {
		/*local*/GtNode CurrentNode = Node;
		while(CurrentNode != null) {
			if(!this.IsEmptyBlock(CurrentNode)) {
				this.VisitingBuilder.AppendIndent();
				CurrentNode.Evaluate(this);
				this.VisitingBuilder.AppendLine(this.SemiColon);
			}
			CurrentNode = CurrentNode.NextNode;
		}
	}
	
	@Override public void VisitIfNode(GtIfNode Node) {
		this.VisitingBuilder.Append("if");
		this.VisitingBuilder.Append("(");
		Node.CondExpr.Evaluate(this);
		this.VisitingBuilder.Append(") ");
		this.VisitIndentBlock("{", Node.ThenNode, "}");
		if(this.DoesNodeExist(Node.ElseNode)) {
			this.VisitingBuilder.Append(" else ");			
			this.VisitIndentBlock("{", Node.ElseNode, "}");
		}
	}

	@Override public void VisitTrinaryNode(GtTrinaryNode Node) {
		this.VisitingBuilder.Append("(");
		Node.ConditionNode.Evaluate(this);
		this.VisitingBuilder.Append(" ? ");
		Node.ThenNode.Evaluate(this);
		this.VisitingBuilder.Append(" : ");
		Node.ElseNode.Evaluate(this);
		this.VisitingBuilder.Append(")");
	}
	@Override public void VisitSwitchNode(GtSwitchNode Node) {
	}

	@Override public void VisitWhileNode(GtWhileNode Node) {
		this.VisitingBuilder.Append("while(");
		Node.CondExpr.Evaluate(this);
		this.VisitingBuilder.Append(") ");
		this.VisitIndentBlock("{", Node.LoopBody, "}");
		this.VisitingBuilder.AppendLine("");
	}
	
	@Override public void VisitDoWhileNode(GtDoWhileNode Node) {
	}

	@Override public void VisitForNode(GtForNode Node) {
	}

	@Override public void VisitForEachNode(GtForEachNode Node) {
		LibGreenTea.TODO("ForEach");
	}

	@Override public void VisitBreakNode(GtBreakNode Node) {
		this.VisitingBuilder.Append("break");
	}

	@Override public void VisitContinueNode(GtContinueNode Node) {
		this.VisitingBuilder.Append("continue");
	}
	@Override public void VisitTryNode(GtTryNode Node) {
	}

	@Override public void VisitThrowNode(GtThrowNode Node) {
	}

	@Override public void VisitInstanceOfNode(GtInstanceOfNode Node) {
	}

	@Override public void VisitCastNode(GtCastNode Node) {
	}

	@Override public void VisitFunctionNode(GtFunctionNode Node) {
		LibGreenTea.TODO("FunctionNode");
	}

	@Override public void VisitErrorNode(GtErrorNode Node) {
//		this.Builder.AsmMethodVisitor.visitLdcInsn("(ErrorNode)");
//		this.Builder.Call(this.methodMap.get("error_node"));
		LibGreenTea.Exit(1, "ErrorNode found in JavaByteCodeGenerator");
	}
	@Override public void VisitCommandNode(GtCommandNode Node) {
	}

	@Override public void InvokeMainFunc(String MainFuncName) {
	}
	
	private final boolean DoesNodeExist(GtNode Node){
		return Node != null && !(Node instanceof GtEmptyNode);
	}
}