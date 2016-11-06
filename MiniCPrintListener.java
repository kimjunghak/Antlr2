import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Created by KJH on 2016-11-05.
 */
public class MiniCPrintListener extends MiniCBaseListener {

    ParseTreeProperty<String> newTexts = new ParseTreeProperty<>();

    boolean isBinaryOperation(MiniCParser.ExprContext ctx){
        return ctx.getChildCount() == 3 && ctx.getChild(1) != ctx.expr();
    }

    boolean isUnaryOperation(MiniCParser.ExprContext ctx){
        return ctx.getChildCount() == 2 & ctx.getChild(0) != ctx.expr();
    }

    boolean isTypeOnly(MiniCParser.Local_declContext ctx){
        return ctx.getChildCount() == 3;
    }

    boolean isTypeArray(MiniCParser.Local_declContext ctx){
        return ctx.getChildCount() == 6;
    }

    boolean isTypeAssign(MiniCParser.Local_declContext ctx){
        return ctx.getChildCount() == 5;
    }

    boolean isVarTypeOnly(MiniCParser.Var_declContext ctx){
        return ctx.getChildCount() == 3;
    }

    boolean isVarTypeArray(MiniCParser.Var_declContext ctx){
        return ctx.getChildCount() == 6;
    }

    boolean isVarTypeAssign(MiniCParser.Var_declContext ctx){
        return ctx.getChildCount() == 5;
    }

    boolean isExpr_stmt(MiniCParser.StmtContext ctx){
        return ctx.getChild(0) == ctx.expr_stmt();
    }

    boolean isCompound_stmt(MiniCParser.StmtContext ctx){
        return ctx.getChild(0) == ctx.compound_stmt();
    }

    boolean isIf_stmt(MiniCParser.StmtContext ctx){
        return ctx.getChild(0) == ctx.if_stmt();
    }

    boolean isWhile_stmt(MiniCParser.StmtContext ctx){
        return ctx.getChild(0) == ctx.while_stmt();
    }

    boolean isReturn_stmt(MiniCParser.StmtContext ctx){
        return ctx.getChild(0) == ctx.return_stmt();
    }

    boolean isNoBraket(MiniCParser.ParamContext ctx){
        return ctx.getChildCount() == 2;
    }

    boolean isBraket(MiniCParser.ParamContext ctx){
        return ctx.getChildCount() == 4;
    }

    boolean isVoid(MiniCParser.ParamsContext ctx){
        return ctx.getChildCount() == 1;
    }

    boolean isNohting(MiniCParser.ParamsContext ctx){
        return ctx.getChildCount() == 0;
    }

    boolean isVarDecl(MiniCParser.DeclContext ctx){
        return ctx.getChild(0) == ctx.var_decl();
    }

    boolean isFunDecl(MiniCParser.DeclContext ctx){
        return ctx.getChild(0) == ctx.fun_decl();
    }

    boolean isArgsNohting(MiniCParser.ArgsContext ctx){
        return ctx.getChildCount() == 0;
    }


    @Override
    public void exitProgram(MiniCParser.ProgramContext ctx) {
        System.out.print(newTexts.get(ctx));
    }

    @Override
    public void exitDecl(MiniCParser.DeclContext ctx) {
        String decl = null;
        if(isVarDecl(ctx)) {
            decl = newTexts.get(ctx.var_decl());
        }
        else if(isFunDecl(ctx)){
            decl = newTexts.get(ctx.fun_decl());
        }

        newTexts.put(ctx, decl);

    }

    @Override
    public void exitVar_decl(MiniCParser.Var_declContext ctx) {
        String type, id, assign, lit, semicolon, leftSquareBracket, rightSquareBracket;

        if(isVarTypeAssign(ctx)) {
            type = newTexts.get(ctx.type_spec());
            id = ctx.getChild(1).getText();
            assign = ctx.getChild(2).getText();
            lit = ctx.getChild(3).getText();
            semicolon = ctx.getChild(4).getText();

            newTexts.put(ctx, type + " "+ id + " " + assign + " " + lit + semicolon + "\n");

        }

        else if(isVarTypeArray(ctx)){
            type = newTexts.get(ctx.type_spec());
            id = ctx.getChild(1).getText();
            leftSquareBracket = ctx.getChild(2).getText();
            lit = ctx.getChild(3).getText();
            rightSquareBracket = ctx.getChild(4).getText();
            semicolon = ctx.getChild(5).getText();

            newTexts.put(ctx, type + " " + id + leftSquareBracket + lit + rightSquareBracket + semicolon + "\n");
        }

        else if(isVarTypeOnly(ctx)){
            type = newTexts.get(ctx.type_spec());
            id = ctx.getChild(0).getText();

            newTexts.put(ctx, type + " " + id);
        }

    }


    @Override
    public void exitType_spec(MiniCParser.Type_specContext ctx) {
        String type;

        type = ctx.getChild(0).getText();

        newTexts.put(ctx, type);

    }

    @Override
    public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
        String type, id, leftParen, rightParen, params, compund_stmt;

        type = newTexts.get(ctx.type_spec());
        id = ctx.getChild(1).getText();
        leftParen = ctx.getChild(2).getText();
        params = newTexts.get(ctx.params());
        rightParen = ctx.getChild(4).getText();
        compund_stmt = newTexts.get(ctx.compound_stmt());

        newTexts.put(ctx, type + " " + id + " " + leftParen + params + rightParen + compund_stmt);
    }

    @Override
    public void exitParams(MiniCParser.ParamsContext ctx) {
        String param1, param2, comma, void_stmt;

        if(isVoid(ctx)){
            void_stmt = ctx.getChild(0).getText();

            newTexts.put(ctx, void_stmt);
        }

        else if(isNohting(ctx)){
            newTexts.put(ctx, "");
        }

        else{
            param1 = newTexts.get(ctx.param(0));
            comma = ctx.getChild(1).getText();
            param2 = newTexts.get(ctx.param(1));

            newTexts.put(ctx, param1 + comma + " " + param2);
        }
    }

    @Override
    public void exitParam(MiniCParser.ParamContext ctx) {
        String type, id, leftSquareBracket, rightSquareBracket;

        if(isNoBraket(ctx)){
            type = newTexts.get(ctx.type_spec());
            id = ctx.getChild(1).getText();

            newTexts.put(ctx, type + " " + id);
        }

        else if(isBraket(ctx)){
            type = newTexts.get(ctx.type_spec());
            id = ctx.getChild(1).getText();
            leftSquareBracket = ctx.getChild(2).getText();
            rightSquareBracket = ctx.getChild(3).getText();

            newTexts.put(ctx, type + " " + id + " " + leftSquareBracket + rightSquareBracket);
        }
    }

    @Override
    public void exitStmt(MiniCParser.StmtContext ctx) {
        String stmt;

        if(isExpr_stmt(ctx)){
            stmt = newTexts.get(ctx.expr_stmt());

            newTexts.put(ctx, stmt);
        }

        else if(isCompound_stmt(ctx)){
            stmt = newTexts.get(ctx.compound_stmt());

            newTexts.put(ctx, stmt);
        }

        else if(isIf_stmt(ctx)){
            stmt = newTexts.get(ctx.if_stmt());

            newTexts.put(ctx, stmt);
        }

        else if(isWhile_stmt(ctx)){
            stmt = newTexts.get(ctx.while_stmt());

            newTexts.put(ctx, stmt);
        }

        else if(isReturn_stmt(ctx)){
            stmt = newTexts.get(ctx.return_stmt());

            newTexts.put(ctx, stmt);
        }
    }

    @Override
    public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
        String expr, semicolon;

        expr = newTexts.get(ctx.expr());
        semicolon = ctx.getChild(1).getText();

        newTexts.put(ctx, expr + semicolon);
    }

    @Override
    public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
        String while_stmt, leftParen, rightParen, expr, stmt;

        while_stmt = ctx.getChild(0).getText();
        leftParen = ctx.getChild(1).getText();
        expr = newTexts.get(ctx.expr());
        rightParen = ctx.getChild(3).getText();
        stmt = newTexts.get(ctx.stmt());

        newTexts.put(ctx, while_stmt + leftParen + expr + rightParen + stmt);
    }

    @Override
    public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
        String leftCurlyBracket, rightCurlyBracket, local, stmt;

        leftCurlyBracket = ctx.getChild(0).getText();
        local = newTexts.get(ctx.local_decl(0));
        stmt = newTexts.get(ctx.stmt(0));
        rightCurlyBracket = ctx.getChild(2).getText();

        newTexts.put(ctx, leftCurlyBracket + "\n...." + local + stmt + rightCurlyBracket);
    }

    @Override
    public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
        String type, id, assign, lit, semicolon, leftSquareBracket, rightSquareBracket;

        if(isTypeAssign(ctx)) {
            type = newTexts.get(ctx.type_spec());
            id = ctx.getChild(1).getText();
            assign = ctx.getChild(2).getText();
            lit = ctx.getChild(3).getText();
            semicolon = ctx.getChild(4).getText();

            newTexts.put(ctx, type + " "+ id + " " + assign + " " + lit + semicolon + "\n");

        }

        else if(isTypeArray(ctx)){
            type = newTexts.get(ctx.type_spec());
            id = ctx.getChild(1).getText();
            leftSquareBracket = ctx.getChild(2).getText();
            lit = ctx.getChild(3).getText();
            rightSquareBracket = ctx.getChild(4).getText();
            semicolon = ctx.getChild(5).getText();

            newTexts.put(ctx, type + " " + id + leftSquareBracket + lit + rightSquareBracket + semicolon + "\n");
        }

        else if(isTypeOnly(ctx)){
            type = newTexts.get(ctx.type_spec());
            id = ctx.getChild(0).getText();

            newTexts.put(ctx, type + " " + id);
        }

    }

    @Override
    public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
        String if_stmt, paren1, paren2, expr, stmt;

        if_stmt = ctx.getChild(0).getText();
        expr = newTexts.get(ctx.expr());
        paren1 = ctx.getChild(1).getText();
        paren2 = ctx.getChild(3).getText();
        stmt = newTexts.get(ctx.stmt(0));

        newTexts.put(ctx, if_stmt + paren1 + expr + paren2 + stmt);

    }

    @Override
    public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
        String ret, s1;

        ret = ctx.getChild(0).getText();
        s1 = newTexts.get(ctx.expr());
        newTexts.put(ctx, ret + " " + s1);

    }

    @Override
    public void exitExpr(MiniCParser.ExprContext ctx){
        String s1, s2, op;

        if(isBinaryOperation(ctx)) {

            s1 = newTexts.get(ctx.expr(0));
            s2 = newTexts.get(ctx.expr(1));
            op = ctx.getChild(1).getText();
            newTexts.put(ctx, s1 + " " + op + " " + s2);

        }

        else if(isUnaryOperation(ctx)){
            op = ctx.getChild(0).getText();
            s1 = newTexts.get(ctx.expr(0));
            newTexts.put(ctx, op + s1);

        }

    }

    @Override
    public void exitArgs(MiniCParser.ArgsContext ctx) {
        String expr1, expr2, comma;

        if(isArgsNohting(ctx)){
            newTexts.put(ctx, "");
        }

        else{
            expr1 = newTexts.get(ctx.expr(0));
            comma = ctx.getChild(1).getText();
            expr2 = newTexts.get(ctx.expr(1));

            newTexts.put(ctx, expr1 + comma + " " + expr2);
        }

    }

}

