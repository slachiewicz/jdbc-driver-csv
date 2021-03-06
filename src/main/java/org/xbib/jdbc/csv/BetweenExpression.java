/*
 *  CsvJdbc - a JDBC driver for CSV files
 *  Copyright (C) 2008  Mario Frasca
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class BetweenExpression extends LogicalExpression {
    Expression obj, left, right;

    public BetweenExpression(Expression obj, Expression left, Expression right) {
        this.obj = obj;
        this.left = left;
        this.right = right;
    }

    public boolean isTrue(Map<String, Object> env) throws SQLException {
        Comparable leftValue = (Comparable) left.eval(env);
        Comparable rightValue = (Comparable) right.eval(env);
        Comparable objValue = (Comparable) obj.eval(env);
        Integer comparedLeft = RelopExpression.compare(leftValue, objValue, env);
        boolean result = false;
        if (comparedLeft != null && comparedLeft.intValue() <= 0) {
            Integer comparedRight = RelopExpression.compare(rightValue, objValue, env);
            if (comparedRight != null && comparedRight.intValue() >= 0) {
                result = true;
            }
        }
        return result;
    }

    public String toString() {
        return "B " + obj + " " + left + " " + right;
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        result.addAll(obj.usedColumns());
        result.addAll(left.usedColumns());
        result.addAll(right.usedColumns());
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        result.addAll(obj.aggregateFunctions());
        result.addAll(left.aggregateFunctions());
        result.addAll(right.aggregateFunctions());
        return result;
    }
}
