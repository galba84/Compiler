package Entities;


import Exceptions.ExpressionException;

import java.util.LinkedList;
import java.util.List;

public class Cycle extends BodyElement {
    Expression condition;
    List<BodyElement> positive = new LinkedList<>();

    public Cycle(Expression c, List<BodyElement> p) {
        condition = c;
        copy(p);
    }

    private void copy(List<BodyElement> p) {

        positive.addAll(p);

    }

    public Expression getCondition() {
        return condition;
    }

    public List<BodyElement> getPositive() {
        return positive;
    }

    @Override
    public String toString() {
        return "Cycle{" +
                "condition=" + condition.toString() +
                "positive=" + positive.toString() +
                '}';
    }

    public void validate() throws ExpressionException {
        if (null != condition) {
            condition.validate();
        }
    }
}
