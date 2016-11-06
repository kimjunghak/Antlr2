import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Created by KJH on 2016-11-05.
 */
public class MiniCPrintListener extends MiniCBaseListener {

    ParseTreeProperty<String> newTexts = new ParseTreeProperty<>();

    private boolean isBinaryOperation(MiniCParser.ExprContext ctx){
        return ctx.getChildCount() == 3 && ctx.getChild(1) != ctx.expr();
    }

    private boolean isUnaryOperation(MiniCParser.ExprContext ctx){
        return ctx.getChildCount() == 2 & ctx.getChild(0) != ctx.expr();
    }

    private boolean isTypeOnly(MiniCParser.Local_declContext ctx){
        return ctx.getChildCount() == 3;
    }

    private boolean isTypeArray(MiniCParser.Local_declContext ctx){
        return ctx.getChildCount() == 6;
    }

    private boolean isTypeAssign(MiniCParser.Local_declContext ctx){
        return ctx.getChildCount() == 5;
    }

    private boolean isVarTypeOnly(MiniCParser.Var_declContext ctx){
        return ctx.getChildCount() == 3;
    }

    private boolean isVarTypeArray(MiniCParser.Var_declContext ctx){
        return ctx.getChildCount() == 6;
    }

    private boolean isVarTypeAssign(MiniCParser.Var_declContext ctx){
        return ctx.getChildCount() == 5;
    }

    private boolean isExpr_stmt(MiniCParser.StmtContext ctx){
        return ctx.getChild(0) == ctx.expr_stmt();
    }

    private boolean isCompound_stmt(MiniCParser.StmtContext ctx){
        return ctx.getChild(0) == ctx.compound_stmt();
    }

    private boolean isIf_stmt(MiniCParser.StmtContext ctx){
        return ctx.getChild(0) == ctx.if_stmt();
    }

    private boolean isWhile_stmt(MiniCParser.StmtContext ctx){
        return ctx.getChild(0) == ctx.while_stmt();
    }

    private boolean isReturn_stmt(MiniCParser.StmtContext ctx){
        return ctx.getChild(0) == ctx.return_stmt();
    }

    private boolean isNoBraket(MiniCParser.ParamContext ctx){
        return ctx.getChildCount() == 2;
    }

    private boolean isBraket(MiniCParser.ParamContext ctx){
        return ctx.getChildCount() == 4;
    }

    private boolean isVoid(MiniCParser.ParamsContext ctx){
        return ctx.getChildCount() == 1;
    }

    private boolean isNohting(MiniCParser.ParamsContext ctx){
        return ctx.getChildCount() == 0;
    }

    private boolean isVarDecl(MiniCParser.DeclContext ctx){
        return ctx.getChild(0) == ctx.var_decl();
    }

    private boolean isFunDecl(MiniCParser.DeclContext ctx){
        return ctx.getChild(0) == ctx.fun_decl();
    }

    private boolean isArgsNohting(MiniCParser.ArgsContext ctx){
        return ctx.getChildCount() == 0;
    }

    private boolean isArrayExpr(MiniCParser.ExprContext ctx) {
        return ctx.getChildCount() == 6 && ctx.getChild(2) == ctx.expr();
    }

    private boolean isIdAssign(MiniCParser.ExprContext ctx) {
        return ctx.getChildCount() == 3 && ctx.getChild(2) == ctx.expr();
    }

    private boolean isIdArgs(MiniCParser.ExprContext ctx) {
        return ctx.getChildCount() == 4 && ctx.getChild(2) == ctx.args();
    }

    private boolean isIdExpr(MiniCParser.ExprContext ctx) {
        return ctx.getChildCount() == 4 && ctx.getChild(2) == ctx.expr();
    }

    private boolean isExpr(MiniCParser.ExprContext ctx) {
        return ctx.getChildCount() == 3 && ctx.getChild(1) == ctx.expr();
    }

    private boolean isIdent(MiniCParser.ExprContext ctx) {
        return ctx.getChild(0) == ctx.IDENT();
    }

    private boolean isLiteral(MiniCParser.ExprContext ctx) {
        return ctx.getChild(0) == ctx.LITERAL();
    }

    private String opSelect(MiniCParser.ExprContext ctx, String op) {
        switch(op){
            case "EQ":
                op = newTexts.get(ctx.EQ());
                break;
            case "NE":
                op = newTexts.get(ctx.NE());
                break;
            case "LE":
                op = newTexts.get(ctx.LE());
                break;
            case "GE":
                op = newTexts.get(ctx.GE());
                break;
            case "AND":
                op = newTexts.get(ctx.AND());
                break;
            case "OR":
                op = newTexts.get(ctx.OR());
                break;
            default:
                break;
        }
        return op;
    }

    private boolean isElse(MiniCParser.If_stmtContext ctx) {
        return ctx.getChildCount() == 7 && ctx.getChild(5) == ctx.ELSE();
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
            id = newTexts.get(ctx.IDENT());
            assign = ctx.getChild(2).getText();
            lit = newTexts.get(ctx.LITERAL());
            semicolon = ctx.getChild(4).getText();

            newTexts.put(ctx, type + " "+ id + " " + assign + " " + lit + semicolon + "\n");

        }

        else if(isVarTypeArray(ctx)){
            type = newTexts.get(ctx.type_spec());
            id = newTexts.get(ctx.IDENT());
            leftSquareBracket = ctx.getChild(2).getText();
            lit = newTexts.get(ctx.LITERAL());
            rightSquareBracket = ctx.getChild(4).getText();
            semicolon = ctx.getChild(5).getText();

            newTexts.put(ctx, type + " " + id + leftSquareBracket + lit + rightSquareBracket + semicolon + "\n");
        }

        else if(isVarTypeOnly(ctx)){
            type = newTexts.get(ctx.type_spec());
            id = newTexts.get(ctx.IDENT());
            semicolon = ctx.getChild(2).getText();

            newTexts.put(ctx, type + " " + id + semicolon + "\n");
        }

    }


    @Override
    public void exitType_spec(MiniCParser.Type_specContext ctx) {
        String type = null;

        if(isTypeVOID(ctx))
            type = newTexts.get(ctx.VOID());

        else if(isTypeINT(ctx))
            type = newTexts.get(ctx.INT());

        newTexts.put(ctx, type);

    }

    private boolean isTypeINT(MiniCParser.Type_specContext ctx) {
        return ctx.getChild(0) == ctx.INT();
    }

    private boolean isTypeVOID(MiniCParser.Type_specContext ctx) {
        return ctx.getChild(0) == ctx.VOID();
    }

    @Override
    public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
        String type, id, leftParen, rightParen, params, compund_stmt;

        type = newTexts.get(ctx.type_spec());
        id = newTexts.get(ctx.IDENT());
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
            void_stmt = newTexts.get(ctx.VOID());

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
            id = newTexts.get(ctx.IDENT());

            newTexts.put(ctx, type + " " + id);
        }

        else if(isBraket(ctx)){
            type = newTexts.get(ctx.type_spec());
            id = newTexts.get(ctx.IDENT());
            leftSquareBracket = ctx.getChild(2).getText();
            rightSquareBracket = ctx.getChild(3).getText();

            newTexts.put(ctx, type + " " + id + " " + leftSquareBracket + rightSquareBracket);
        }
    }

    @Override
    public void exitStmt(MiniCParser.StmtContext ctx) {
        String stmt = null;

        if(isExpr_stmt(ctx))
            stmt = newTexts.get(ctx.expr_stmt());

        else if(isCompound_stmt(ctx))
            stmt = newTexts.get(ctx.compound_stmt());

        else if(isIf_stmt(ctx))
            stmt = newTexts.get(ctx.if_stmt());

        else if(isWhile_stmt(ctx))
            stmt = newTexts.get(ctx.while_stmt());

        else if(isReturn_stmt(ctx))
            stmt = newTexts.get(ctx.return_stmt());

        newTexts.put(ctx, stmt);
    }

    @Override
    public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
        String expr, semicolon;

        expr = newTexts.get(ctx.expr());
        semicolon = ctx.getChild(1).getText();

        newTexts.put(ctx, expr + semicolon + "\n");
    }

    @Override
    public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
        String while_stmt, leftParen, rightParen, expr, stmt;

        while_stmt = newTexts.get(ctx.WHILE());
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
            id = newTexts.get(ctx.IDENT());
            assign = ctx.getChild(2).getText();
            lit = newTexts.get(ctx.LITERAL());
            semicolon = ctx.getChild(4).getText();

            newTexts.put(ctx, type + " "+ id + " " + assign + " " + lit + semicolon + "\n");

        }

        else if(isTypeArray(ctx)){
            type = newTexts.get(ctx.type_spec());
            id = newTexts.get(ctx.IDENT());
            leftSquareBracket = ctx.getChild(2).getText();
            lit = newTexts.get(ctx.LITERAL());
            rightSquareBracket = ctx.getChild(4).getText();
            semicolon = ctx.getChild(5).getText();

            newTexts.put(ctx, type + " " + id + leftSquareBracket + lit + rightSquareBracket + semicolon + "\n");
        }

        else if(isTypeOnly(ctx)){
            type = newTexts.get(ctx.type_spec());
            id = newTexts.get(ctx.IDENT());
            semicolon = ctx.getChild(2).getText();

            newTexts.put(ctx, type + " " + id + semicolon + "\n");
        }

    }

    @Override
    public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
        String if_stmt, leftParen, rightParen, expr, stmt1, else_stmt, stmt2;

        if(isElse(ctx)){
            if_stmt = newTexts.get(ctx.IF());
            leftParen = ctx.getChild(1).getText();
            expr = newTexts.get(ctx.expr());
            rightParen = ctx.getChild(3).getText();
            stmt1 = newTexts.get(ctx.stmt(0));
            else_stmt = newTexts.get(ctx.ELSE());
            stmt2 = newTexts.get(ctx.stmt(1));

            newTexts.put(ctx, if_stmt + leftParen + expr + rightParen + stmt1 + else_stmt + stmt2);
        }

        else if(ctx.getChildCount() == 4){
            if_stmt = newTexts.get(ctx.IF());
            leftParen = ctx.getChild(1).getText();
            expr = newTexts.get(ctx.expr());
            rightParen = ctx.getChild(3).getText();
            stmt1 = newTexts.get(ctx.stmt(0));

            newTexts.put(ctx, if_stmt + leftParen + expr + rightParen + stmt1);
        }


    }


    @Override
    public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
        String ret, expr, semicolon;

        if(ctx.getChildCount() == 3) {
            ret = newTexts.get(ctx.RETURN());
            expr = newTexts.get(ctx.expr());
            semicolon = ctx.getChild(2).getText();
            newTexts.put(ctx, ret + " " + expr + semicolon + "\n");
        }
        else if(ctx.getChildCount() == 2){
            ret = newTexts.get(ctx.RETURN());
            semicolon = ctx.getChild(1).getText();

            newTexts.put(ctx, ret + semicolon + "\n");
        }

    }

    @Override
    public void exitExpr(MiniCParser.ExprContext ctx){
        String expr1, expr2, op, leftParen, rightParen, args, id, assign;

        if(isLiteral(ctx)){
            expr1 = newTexts.get(ctx.LITERAL());
            newTexts.put(ctx, expr1);
        }

        else if(isIdent(ctx)){
            expr1 = newTexts.get(ctx.IDENT());
            newTexts.put(ctx, expr1);
        }

        else if(isExpr(ctx)){
            leftParen = ctx.getChild(0).getText();
            rightParen = ctx.getChild(2).getText();
            expr1 = newTexts.get(ctx.expr(0));

            newTexts.put(ctx, leftParen + expr1 + rightParen);
        }

        else if(isIdExpr(ctx)){
            id = newTexts.get(ctx.IDENT());
            leftParen = ctx.getChild(1).getText();
            rightParen = ctx.getChild(3).getText();
            expr1 = newTexts.get(ctx.expr(0));

            newTexts.put(ctx, id + leftParen + expr1 + rightParen);
        }

        else if(isIdArgs(ctx)){
            id = newTexts.get(ctx.IDENT());
            leftParen = ctx.getChild(1).getText();
            rightParen = ctx.getChild(3).getText();
            args = newTexts.get(ctx.args());

            newTexts.put(ctx, id + leftParen + args + rightParen);
        }

        else if(isUnaryOperation(ctx)){
            op = ctx.getChild(0).getText();
            expr1 = newTexts.get(ctx.expr(0));
            newTexts.put(ctx, op + expr1);

        }

        else if(isBinaryOperation(ctx)) {

            expr1 = newTexts.get(ctx.expr(0));
            expr2 = newTexts.get(ctx.expr(1));
            op = ctx.getChild(1).getText();
            op = opSelect(ctx, op);
            newTexts.put(ctx, expr1 + " " + op + " " + expr2);

        }

        else if(isIdAssign(ctx)){
            id = newTexts.get(ctx.IDENT());
            assign = ctx.getChild(1).getText();
            expr1 = newTexts.get(ctx.expr(0));

            newTexts.put(ctx, id + " " + assign + " " + expr1);
        }

        else if(isArrayExpr(ctx)){
            id = newTexts.get(ctx.IDENT());
            leftParen = ctx.getChild(1).getText();
            rightParen = ctx.getChild(3).getText();
            expr1 = newTexts.get(ctx.expr(0));
            assign = ctx.getChild(4).getText();
            expr2 = newTexts.get(ctx.expr(1));

            newTexts.put(ctx, id + leftParen + expr1 + rightParen + " " + assign + " " + expr2);
        }

    }


    @Override
    public void exitArgs(MiniCParser.ArgsContext ctx) {
        String expr;

        if(isArgsNohting(ctx)){
            newTexts.put(ctx, "");
        }
        else{
            expr = newTexts.get(ctx.expr(0));
            newTexts.put(ctx, expr);
        }
    }

}

