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

    @Override
    public void exitExpr(MiniCParser.ExprContext ctx){
        String s1 = null, s2 = null, op = null;

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

        System.out.println(newTexts.get(ctx));

    }
    @Override
    public void exitType_spec(MiniCParser.Type_specContext ctx) {
        String s1 = null, s2 = null;

        s1 = ctx.getChild(0).getText();
        newTexts.put(ctx, s1+ " ");

        System.out.println(newTexts.get(ctx));
    }

    @Override
    public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
        String paren1 = null, paren2 = null, s1 = null, s2 = null;

        paren1 = ctx.getChild(1).getText();
        paren2 = ctx.getChild(3).getText();
        s1 = ctx.getChild(2).getText();
        s2 = newTexts.get(ctx.expr());

        newTexts.put(ctx, paren1 + s1 + " " + s2 + paren2);

        System.out.println(newTexts.get(ctx));

    }

    @Override
    public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {

    }

    @Override
    public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
        String s1 = null, s2 = null, paren1 = null, paren2 = null;

        paren1 = ctx.getChild(0).getText();
        paren2 = ctx.getChild(2).getText();


    }
}
