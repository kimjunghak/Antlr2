import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.StringTokenizer;

/**
 * Created by KJH on 2016-11-05.
 */
public class MiniCPrintListener extends MiniCBaseListener {

    ParseTreeProperty<String> newTexts = new ParseTreeProperty<>();

    int indent = 0;

    private boolean isBinaryOperation(MiniCParser.ExprContext ctx){
        return ctx.getChildCount() == 3 && ctx.getChild(1) != ctx.expr();
    }

    private String indent(){
        String str_indent = "";

        for(int i=0 ; i<this.indent ; i++)
            str_indent += "....";

        return str_indent;
    }

    private String select_stmt(MiniCParser.StmtContext ctx, String stmt) {
        if(ctx.getChild(0) == ctx.expr_stmt())
            stmt = newTexts.get(ctx.expr_stmt());

        else if(ctx.getChild(0) == ctx.compound_stmt())
            stmt = newTexts.get(ctx.compound_stmt());

        else if(ctx.getChild(0) == ctx.if_stmt())
            stmt = newTexts.get(ctx.if_stmt());

        else if(ctx.getChild(0) == ctx.while_stmt())
            stmt = newTexts.get(ctx.while_stmt());

        else if(ctx.getChild(0) == ctx.return_stmt())
            stmt = newTexts.get(ctx.return_stmt());

        return stmt;
    }

    @Override
    public void exitProgram(MiniCParser.ProgramContext ctx) {
        String str = "";
        for(int i=0 ; i<ctx.decl().size() ; i++)
            str += newTexts.get(ctx.decl(i)) + "\n";

        StringTokenizer strToken = new StringTokenizer(str, "\n");
        String result = "";
        while(strToken.hasMoreTokens()){
            String token = strToken.nextToken();

            if(token.equals("."))
                result += "\n";

            else {
                if (token.equals("{")) {
                    result += indent() + token + "\n";
                    indent++;
                } else if (token.equals("}")) {
                    indent--;
                    result += indent() + token + "\n";
                } else {
                    result += indent() + token + "\n";
                }
            }
        }

        System.out.println(result);
    }

    @Override
    public void exitDecl(MiniCParser.DeclContext ctx) {

        if(ctx.getChild(0) == ctx.var_decl())
            newTexts.put(ctx, newTexts.get(ctx.var_decl()));
        else
            newTexts.put(ctx, newTexts.get(ctx.fun_decl()));
    }

    @Override
    public void exitVar_decl(MiniCParser.Var_declContext ctx) {
        String type, id;

        type = newTexts.get(ctx.type_spec());
        id = ctx.getChild(1).getText();

        if(ctx.getChildCount() == 3){
            newTexts.put(ctx, type + " " + id + ";");
        }

        String lit = ctx.getChild(3).getText();

        if(ctx.getChildCount() == 5)
            newTexts.put(ctx, type + " " + id + " = " + lit + ";");
        else
            newTexts.put(ctx, type + " " + id + "[" + lit + "];");
    }

    @Override
    public void exitType_spec(MiniCParser.Type_specContext ctx) {
        newTexts.put(ctx, ctx.getChild(0).getText());
    }

    @Override
    public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
        String type, id, parmas, compound;
        type = newTexts.get(ctx.type_spec());
        parmas = newTexts.get(ctx.params());
        compound = newTexts.get(ctx.compound_stmt());
        id = ctx.getChild(1).getText();

        newTexts.put(ctx, type + " " + id + "(" + parmas + ")\n" + compound);
    }

    @Override
    public void exitParams(MiniCParser.ParamsContext ctx) {
        String param;

        if(ctx.getChildCount() == 0)
            newTexts.put(ctx, "");

        else if(ctx.getChildCount() == 1)
            newTexts.put(ctx, ctx.getChild(0).getText());

        else {
            param = newTexts.get(ctx.param(0));
            for (int i = 1; i < ctx.param().size(); i++)
                param += ", " + newTexts.get(ctx.param(i));

            newTexts.put(ctx, param);
        }
    }

    @Override
    public void exitParam(MiniCParser.ParamContext ctx) {
        String type, id;
        type = newTexts.get(ctx.type_spec());
        id = ctx.getChild(1).getText();

        if(ctx.getChildCount() == 2)
            newTexts.put(ctx, type + " " + id);
        else
            newTexts.put(ctx, type + " " + id + "[]");
    }

    @Override
    public void exitStmt(MiniCParser.StmtContext ctx) {
        String stmt = null;

        stmt = select_stmt(ctx, stmt);

        newTexts.put(ctx, stmt);
    }


    @Override
    public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
        String expr = newTexts.get(ctx.expr());

        newTexts.put(ctx, expr + ";");
    }

    @Override
    public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
        String while_stmt, expr, stmt;

        while_stmt = ctx.getChild(0).getText();
        expr = newTexts.get(ctx.expr());
        stmt = newTexts.get(ctx.stmt());
        if(newTexts.get(ctx.stmt().compound_stmt()) == null){
            stmt = "{\n" + stmt + "\n}";
        }

        newTexts.put(ctx, while_stmt + " (" + expr + ")\n" + stmt);
    }

    @Override
    public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
        String str = "";

        for(int i=0 ; i<ctx.local_decl().size() ; i++)
            str += newTexts.get(ctx.local_decl(i)) + "\n";

        if(ctx.local_decl().size() != 0){
            str += ".\n";
        }

        for(int i=0 ; i<ctx.stmt().size() ; i++)
            str += newTexts.get(ctx.stmt(i)) + "\n";


        newTexts.put(ctx, "{\n" + str + "}");
    }

    @Override
    public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
        String type, id;
        type = newTexts.get(ctx.type_spec());
        id = ctx.getChild(1).getText();

        if(ctx.getChildCount() == 3){
            newTexts.put(ctx, type + " " + id + ";");
        }

        else if(ctx.getChildCount() == 5) {
            String lit = ctx.getChild(3).getText();
            newTexts.put(ctx, type + " " + id + " = " + lit + ";");
        }
        else{
            String lit = ctx.getChild(3).getText();
            newTexts.put(ctx, type + " " + id + "[" + lit + "];");
        }
    }

    @Override
    public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
        String if_stmt, expr, stmt1, stmt2, else_stmt;

        if_stmt = ctx.getChild(0).getText();
        expr = newTexts.get(ctx.expr());
        stmt1 = newTexts.get(ctx.stmt(0));

        if(newTexts.get(ctx.stmt(0).compound_stmt()) == null){
            stmt1 = "{\n" + stmt1 + "\n}";
        }

        if(ctx.getChildCount() == 5)
            newTexts.put(ctx, if_stmt + " (" + expr + ")\n" + stmt1);

        else{
            else_stmt = ctx.getChild(5).getText();
            stmt2 = newTexts.get(ctx.stmt(1));
            newTexts.put(ctx, if_stmt + " (" + expr + ")\n" + stmt1 + "\n" + else_stmt + "\n" + stmt2);
        }
    }


    @Override
    public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
        String ret, expr;

        ret = ctx.getChild(0).getText();

        if(ctx.getChildCount() == 2)
            newTexts.put(ctx, ret + ";");

        else{
            expr = newTexts.get(ctx.expr());
            newTexts.put(ctx, ret + " " + expr + ";");
        }
    }

    @Override
    public void exitExpr(MiniCParser.ExprContext ctx){
        String id, expr1, expr2, args, op;

        expr1 = newTexts.get(ctx.expr(0));

        if(ctx.getChildCount() == 1)
            newTexts.put(ctx, ctx.getChild(0).getText());

        else if(ctx.getChild(1) == ctx.expr() && ctx.getChildCount() == 3){
            newTexts.put(ctx, "(" + expr1 + ")");
        }

        else if(ctx.getChildCount() == 2){
            op = ctx.getChild(0).getText();
            newTexts.put(ctx, op + expr1);
        }

        else if(ctx.getChildCount() == 4){
            id = ctx.getChild(0).getText();
            if(ctx.getChild(2) == ctx.expr())
                newTexts.put(ctx, id + "[" + expr1 + "]");
            else{
                args = newTexts.get(ctx.args());
                newTexts.put(ctx, id + "(" + args + ")" );
            }
        }

        else if(isBinaryOperation(ctx)){
            op = ctx.getChild(1).getText();
            if(ctx.getChild(1).getText().equals("=")){
                id = ctx.getChild(0).getText();
                newTexts.put(ctx, id + " " + op + " " + expr1);
            }
            else{
                expr2 = newTexts.get(ctx.expr(1));
                newTexts.put(ctx, expr1 + " " + op + " " + expr2);
            }
        }

        else{
            id = ctx.getChild(0).getText();
            expr2 = newTexts.get(ctx.expr(1));
            newTexts.put(ctx, id + "[" + expr1 +"]" + " = " + expr2);
        }
    }

    @Override
    public void exitArgs(MiniCParser.ArgsContext ctx) {
        String expr = newTexts.get(ctx.expr(0));

        for(int i=1 ; i<ctx.expr().size() ; i++)
            expr = ", " +  newTexts.get(ctx.expr(i));

        newTexts.put(ctx, expr);
    }

}

