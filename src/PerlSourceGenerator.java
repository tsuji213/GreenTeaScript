import java.util.ArrayList;

//GreenTea Generator should be written in each language.

public class PerlSourceGenerator extends GreenTeaGenerator {
	PerlSourceGenerator(String LangName) {
		super("Perl");
	}

	public void VisitEach(TypedNode Node) {
		String Code = "{\n";
		this.Indent();
		/*local*/TypedNode CurrentNode = Node;
		while(CurrentNode != null) {
			CurrentNode.Evaluate(this);
			Code += this.GetIndentString() + this.PopSourceCode() + ";\n";
			CurrentNode = CurrentNode.NextNode;
		}
		this.UnIndent();
		Code += this.GetIndentString() + "}";
		this.PushSourceCode(Code);
	}

	public void VisitEmptyNode(TypedNode Node) {	
	}

	public void VisitSuffixNode(SuffixNode Node) {
		GtMethod Method = Node.Method;
		if(Method.MethodName.equals("++")) {
		} else if(Method.MethodName.equals("--")) {
		} else {
			throw new RuntimeException("NotSupportOperator");
		}
		Node.Expr.Evaluate(this);
		this.PushSourceCode(this.PopSourceCode() + Method.MethodName);
	}

	public void VisitUnaryNode(UnaryNode Node) {
		GtMethod Method = Node.Method;
		if(Method.MethodName.equals("+")) {
		} else if(Method.MethodName.equals("-")) {
		} else if(Method.MethodName.equals("~")) {
		} else if(Method.MethodName.equals("!")) {
		} else if(Method.MethodName.equals("++")) {
		} else if(Method.MethodName.equals("--")) {
		} else {
			throw new RuntimeException("NotSupportOperator");
		}
		Node.Expr.Evaluate(this);
		this.PushSourceCode(Method.MethodName + this.PopSourceCode());
	}

	public void VisitIndexerNode(IndexerNode Node) {
		Node.Indexer.Evaluate(this);
		Node.Expr.Evaluate(this);
		this.PushSourceCode(this.PopSourceCode() + "[" + this.PopSourceCode() + "]");
		
	}

	public void VisitMessageNode(MessageNode Node) {
		// TODO Auto-generated method stub
		
	}

	public void VisitWhileNode(WhileNode Node) {
		Node.CondExpr.Evaluate(this);
		String Program = "while(" + this.PopSourceCode() + ")";
		this.VisitEach(Node.LoopBody);
		Program += this.PopSourceCode();
		this.UnIndent();
		this.PushSourceCode(Program);
	}

	public void VisitDoWhileNode(DoWhileNode Node) {		
		String Program = "do";
		this.VisitEach(Node.LoopBody);
		Node.CondExpr.Evaluate(this);
		Program += " while(" + this.PopSourceCode() + ")";
		this.PushSourceCode(Program);
	}

	public void VisitForNode(ForNode Node) {
		Node.IterExpr.Evaluate(this);
		Node.CondExpr.Evaluate(this);
		Node.InitNode.Evaluate(this);
		String Init = this.PopSourceCode();
		String Cond = this.PopSourceCode();
		String Iter = this.PopSourceCode();
		
		String Program = "for(" + Init + "; " + Cond  + "; " + Iter + ")";
		Node.LoopBody.Evaluate(this);
		Program += this.PopSourceCode();
		this.PushSourceCode(Program);		
	}

	public void VisitForEachNode(ForEachNode Node) {
		// TODO Auto-generated method stub
		
	}

	public void VisitConstNode(ConstNode Node) {
		this.PushSourceCode(Node.ConstValue.toString());
	}

	public void VisitNewNode(NewNode Node) {
		String Type = Node.Type.ShortClassName;
		this.PushSourceCode("new " + Type);

	}

	public void VisitNullNode(NullNode Node) {
		this.PushSourceCode("NULL");
	}

	public void VisitLocalNode(LocalNode Node) {
		this.PushSourceCode("$" + Node.LocalName);
	}

	public void VisitGetterNode(GetterNode Node) {
		Node.Expr.Evaluate(this);
		this.PushSourceCode(this.PopSourceCode() + "->" + Node.Method.MethodName);
	}

	private String[] EvaluateParam(ArrayList<TypedNode> Params) {
		int Size = Params.size();
		String[] Programs = new String[Size];
		for(int i = 0; i < Size; i++) {
			TypedNode Node = Params.get(i);
			Node.Evaluate(this);
			Programs[Size - i - 1] = this.PopSourceCode();
		}
		return Programs;
	}

	public void VisitApplyNode(ApplyNode Node) {
		/*local*/String Program = "&" + Node.Method.MethodName + "(";
		/*local*/String[] Params = EvaluateParam(Node.Params);
		for(int i = 0; i < Params.length; i++) {
			String P = Params[i];
			if(i != 0) {
				Program += ",";
			}
			Program += P;
		}
		Program += ")";
		this.PushSourceCode(Program);
	}

	public void VisitBinaryNode(BinaryNode Node) {
		String MethodName = Node.Method.MethodName;
		if(MethodName.equals("+")) {
		} else if(MethodName.equals("-")) {
		} else if(MethodName.equals("*")) {
		} else if(MethodName.equals("/")) {
		} else if(MethodName.equals("%")) {
		} else if(MethodName.equals("<<")) {
		} else if(MethodName.equals(">>")) {
		} else if(MethodName.equals("&")) {
		} else if(MethodName.equals("|")) {
		} else if(MethodName.equals("^")) {
		} else if(MethodName.equals("<=")) {
		} else if(MethodName.equals("<")) {
		} else if(MethodName.equals(">=")) {
		} else if(MethodName.equals(">")) {
		} else if(MethodName.equals("!=")) {
			if(Node.Method.GetRecvType().ShortClassName.equals("String")) {
				MethodName = "ne";
			}
		} else if(MethodName.equals("==")) {
			if(Node.Method.GetRecvType().ShortClassName.equals("String")) {
				MethodName = "eq";
			}
		} else {
			throw new RuntimeException("NotSupportOperator");
		}
		Node.RightNode.Evaluate(this);
		Node.LeftNode.Evaluate(this);
		this.PushSourceCode(this.PopSourceCode() + " " + MethodName + " " + this.PopSourceCode());
	}

	public void VisitAndNode(AndNode Node) {
		Node.RightNode.Evaluate(this);
		Node.LeftNode.Evaluate(this);
		this.PushSourceCode(this.PopSourceCode() + " && " + this.PopSourceCode());
	}

	public void VisitOrNode(OrNode Node) {
		Node.RightNode.Evaluate(this);
		Node.LeftNode.Evaluate(this);
		this.PushSourceCode(this.PopSourceCode() + " || " + this.PopSourceCode());
	}

	public void VisitAssignNode(AssignNode Node) {
		Node.RightNode.Evaluate(this);
		Node.LeftNode.Evaluate(this);
		this.PushSourceCode(this.PopSourceCode() + " = " + this.PopSourceCode());
	}

	public void VisitLetNode(LetNode Node) {
		String Type = Node.DeclType.ShortClassName;
		Node.VarNode.Evaluate(this);
		String Code = Type + " " + this.PopSourceCode();
		Node.BlockNode.Evaluate(this);
		this.PushSourceCode(Code + this.PopSourceCode());
	}

	public void VisitIfNode(IfNode Node) {
		Node.CondExpr.Evaluate(this);
		this.VisitEach(Node.ThenNode);
		this.VisitEach(Node.ElseNode);

		String ElseBlock = this.PopSourceCode();
		String ThenBlock = this.PopSourceCode();
		String CondExpr = this.PopSourceCode();
		String Code = "if(" + CondExpr + ") " + ThenBlock;
		if(Node.ElseNode != null) {
			Code += " else " + ElseBlock;
		}
		this.PushSourceCode(Code);
		
	}

	public void VisitSwitchNode(SwitchNode Node) {
		// TODO Auto-generated method stub
		
	}

	public void VisitLoopNode(LoopNode Node) {
		// TODO Auto-generated method stub
		
	}

	public void VisitReturnNode(ReturnNode Node) {
		String Code = "return";
		if(Node.Expr != null) {
			Node.Expr.Evaluate(this);
			Code += " " + this.PopSourceCode();
		}
		this.PushSourceCode(Code);
	}

	public void VisitLabelNode(LabelNode Node) {
		String Label = Node.Label;
		this.PushSourceCode(Label + ":");
	}

	public void VisitJumpNode(JumpNode Node) {
		String Label = Node.Label;
		this.PushSourceCode("goto " + Label);
	}

	public void VisitBreakNode(BreakNode Node) {
		String Code = "break";
		String Label = Node.Label;
		if(Label != null) {
			Code += " " + Label;
		}
		this.PushSourceCode(Code);
	}

	public void VisitContinueNode(ContinueNode Node) {
		String Code = "continue";
		String Label = Node.Label;
		if(Label != null) {
			Code += " " + Label;
		}
		this.PushSourceCode(Code);
	}

	public void VisitTryNode(TryNode Node) {
		String Code = "try";
		//this.VisitEach(Node.CatchBlock);
		this.VisitEach(Node.TryBlock);
		
		Code += this.PopSourceCode();
		if(Node.FinallyBlock != null) {
			this.VisitEach(Node.FinallyBlock);
			Code += " finally " + this.PopSourceCode();
		}
		this.PushSourceCode(Code);
	}

	public void VisitThrowNode(ThrowNode Node) {
		Node.Expr.Evaluate(this);
		String Code = "throw " + this.PopSourceCode();
		this.PushSourceCode(Code);
	}

	public void VisitFunctionNode(FunctionNode Node) {
		// TODO Auto-generated method stub
		
	}

	public void VisitErrorNode(ErrorNode Node) {
		String Code = "throw Error(\"" + Node.Token.ParsedText + "\")";
		this.PushSourceCode(Code);
		
	}

	public void DefineFunction(GtMethod Method, ArrayList<String> ParamNameList, TypedNode Body) {
		String Program = "";
		String RetTy = Method.GetReturnType().ShortClassName;
		String ThisTy = Method.GetRecvType().ShortClassName;
		String FuncName = ThisTy + "_" + Method.MethodName;
		String Signature = "# ";
		String Arguments = "";
		Signature += RetTy + " " + FuncName + "(";
		Signature += ThisTy + " this";
		this.Indent();

		Arguments += this.GetIndentString() + "my $this = $_[0]\n";
		for(int i = 0; i < ParamNameList.size(); i++) {
			String ParamTy = Method.GetParamType(i).ShortClassName;
			Signature += " ," + ParamTy + " " + ParamNameList.get(i);
			Arguments += this.GetIndentString() + "my $" + ParamNameList.get(i) + " = $_[" + (i + 1) + "]\n";
		}
		this.UnIndent();
		Program += Signature + "\n" + this.GetIndentString() + "sub " + FuncName + "{\n";
		this.Indent();
		Program += Arguments;
		this.Indent();
		Program += Eval(Body);
		this.UnIndent();
		this.UnIndent();
		Program += Program + "\n" + this.GetIndentString() + "}";
		DebugP(Program);
	}

	public Object Eval(TypedNode Node) {
		this.VisitEach(Node);
		return this.PopSourceCode();
	}

	public void AddClass(GtType Type) {
		// TODO Auto-generated method stub
		
	}

	public void LoadContext(GtContext Context) {
		// TODO Auto-generated method stub
		
	}
}

