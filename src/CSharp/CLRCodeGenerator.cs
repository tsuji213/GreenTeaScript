using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Linq.Expressions;

public partial class CLRCodeGenerator: GtGenerator {

    private Stack<Expression> CLRExpressionStack = new Stack<Expression>();

    public CLRCodeGenerator/*constructor*/(string TargetCode, string OutputFile, int GeneratorFlag) :base(TargetCode, OutputFile, GeneratorFlag){
	}
	override public void GenerateFunc(GtFunc Func, List<string> ParamNameList, GtNode Body) {
		string MethodName = Func.GetNativeFuncName();
        //GtSourceBuilder Builder = new GtSourceBuilder(this);
        //Builder.IndentAndAppend("function ");
        //Builder.SpaceAppendSpace(MethodName);
        //Builder.Append("(");
        //int i = 0;
        //int size = LibGreenTea.ListSize(ParamNameList);
        //while(i < size) {
        //    if(i != 0) {
        //        Builder.Append(this.Camma);
        //    }
        //    Builder.Append(ParamNameList[i]);
        //    i += 1;
        //}
        //Builder.Append(")");
        //GtSourceBuilder PushedBuilder = this.VisitingBuilder;
        //this.VisitingBuilder = Builder;
        //this.VisitIndentBlock("{", Body, "}");
        //this.VisitingBuilder = PushedBuilder;
		//Console.WriteLine(Builder);

        var Parameters = new List<ParameterExpression>();
        for (int i = 0; i < Func.GetFuncParamSize(); i++)
        {
            var CLRType = Type.GetType(Func.GetFuncParamType(i).GetNativeName());
            Parameters.Add(Expression.Parameter(CLRType, ParamNameList[i]));
        }

        //var x = Expression.Parameter(typeof(int), "x");
        //var t = Expression.Parameter(typeof(int), "t");
        var endLoop = Expression.Label("EndLoop");

        

        // 本当はコード生成の結果として得られる
        //var statements = new Expression[]{Expression.Assign(x, Expression.Constant(0)),
        //    Expression.Loop(
        //        Expression.Block(
        //            Expression.AddAssign(x, i),
        //            Expression.SubtractAssign(i, Expression.Constant(1)),
        //            Expression.IfThen(
        //                Expression.LessThan(i, Expression.Constant(0)),
        //                Expression.Break(endLoop))),
        //        endLoop),
        //    x};

        //this.VisitIndentBlock("{", Body, "}");
        Body.Evaluate(this);
        var statements = CLRExpressionStack.Pop();

        var ReturnType = typeof(int);

        var body = Expression.Block(ReturnType, Parameters, statements);

        //var e = Expression.Lambda<Func<int, int>>(body, t);

        //var f = e.Compile();
        //Console.WriteLine(e);
	}

    override public void VisitVarNode(GtVarNode Node) {
		var x = Expression.Parameter(typeof(int), "x");
        CLRExpressionStack.Push(x);
	}

    override public void VisitConstNode(GtConstNode Node) {
        //this.VisitingBuilder.Append(Node.Token.ParsedText);
	}

    override public void VisitReturnNode(GtReturnNode Node) {
        //this.VisitingBuilder.Append("return");
        //if(this.DoesNodeExist(Node.Expr)){
        //    this.VisitingBuilder.Append(" ");
        //    Node.Expr.Evaluate(this);
        //}
	}

}
