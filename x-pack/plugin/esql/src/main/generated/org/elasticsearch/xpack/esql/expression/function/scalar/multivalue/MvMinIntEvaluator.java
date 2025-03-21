// Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
// or more contributor license agreements. Licensed under the Elastic License
// 2.0; you may not use this file except in compliance with the Elastic License
// 2.0.
package org.elasticsearch.xpack.esql.expression.function.scalar.multivalue;

import java.lang.Override;
import java.lang.String;
import org.elasticsearch.compute.data.Block;
import org.elasticsearch.compute.data.IntBlock;
import org.elasticsearch.compute.data.IntVector;
import org.elasticsearch.compute.operator.DriverContext;
import org.elasticsearch.compute.operator.EvalOperator;

/**
 * {@link EvalOperator.ExpressionEvaluator} implementation for {@link MvMin}.
 * This class is generated. Edit {@code MvEvaluatorImplementer} instead.
 */
public final class MvMinIntEvaluator extends AbstractMultivalueFunction.AbstractEvaluator {
  public MvMinIntEvaluator(EvalOperator.ExpressionEvaluator field, DriverContext driverContext) {
    super(driverContext, field);
  }

  @Override
  public String name() {
    return "MvMin";
  }

  /**
   * Evaluate blocks containing at least one multivalued field.
   */
  @Override
  public Block evalNullable(Block fieldVal) {
    if (fieldVal.mvSortedAscending()) {
      return evalAscendingNullable(fieldVal);
    }
    IntBlock v = (IntBlock) fieldVal;
    int positionCount = v.getPositionCount();
    try (IntBlock.Builder builder = driverContext.blockFactory().newIntBlockBuilder(positionCount)) {
      for (int p = 0; p < positionCount; p++) {
        int valueCount = v.getValueCount(p);
        if (valueCount == 0) {
          builder.appendNull();
          continue;
        }
        int first = v.getFirstValueIndex(p);
        int end = first + valueCount;
        int value = v.getInt(first);
        for (int i = first + 1; i < end; i++) {
          int next = v.getInt(i);
          value = MvMin.process(value, next);
        }
        int result = value;
        builder.appendInt(result);
      }
      return builder.build();
    }
  }

  /**
   * Evaluate blocks containing at least one multivalued field.
   */
  @Override
  public Block evalNotNullable(Block fieldVal) {
    if (fieldVal.mvSortedAscending()) {
      return evalAscendingNotNullable(fieldVal);
    }
    IntBlock v = (IntBlock) fieldVal;
    int positionCount = v.getPositionCount();
    try (IntVector.FixedBuilder builder = driverContext.blockFactory().newIntVectorFixedBuilder(positionCount)) {
      for (int p = 0; p < positionCount; p++) {
        int valueCount = v.getValueCount(p);
        int first = v.getFirstValueIndex(p);
        int end = first + valueCount;
        int value = v.getInt(first);
        for (int i = first + 1; i < end; i++) {
          int next = v.getInt(i);
          value = MvMin.process(value, next);
        }
        int result = value;
        builder.appendInt(result);
      }
      return builder.build().asBlock();
    }
  }

  /**
   * Evaluate blocks containing at least one multivalued field and all multivalued fields are in ascending order.
   */
  private Block evalAscendingNullable(Block fieldVal) {
    IntBlock v = (IntBlock) fieldVal;
    int positionCount = v.getPositionCount();
    try (IntBlock.Builder builder = driverContext.blockFactory().newIntBlockBuilder(positionCount)) {
      for (int p = 0; p < positionCount; p++) {
        int valueCount = v.getValueCount(p);
        if (valueCount == 0) {
          builder.appendNull();
          continue;
        }
        int first = v.getFirstValueIndex(p);
        int idx = MvMin.ascendingIndex(valueCount);
        int result = v.getInt(first + idx);
        builder.appendInt(result);
      }
      return builder.build();
    }
  }

  /**
   * Evaluate blocks containing at least one multivalued field and all multivalued fields are in ascending order.
   */
  private Block evalAscendingNotNullable(Block fieldVal) {
    IntBlock v = (IntBlock) fieldVal;
    int positionCount = v.getPositionCount();
    try (IntVector.FixedBuilder builder = driverContext.blockFactory().newIntVectorFixedBuilder(positionCount)) {
      for (int p = 0; p < positionCount; p++) {
        int valueCount = v.getValueCount(p);
        int first = v.getFirstValueIndex(p);
        int idx = MvMin.ascendingIndex(valueCount);
        int result = v.getInt(first + idx);
        builder.appendInt(result);
      }
      return builder.build().asBlock();
    }
  }

  public static class Factory implements EvalOperator.ExpressionEvaluator.Factory {
    private final EvalOperator.ExpressionEvaluator.Factory field;

    public Factory(EvalOperator.ExpressionEvaluator.Factory field) {
      this.field = field;
    }

    @Override
    public MvMinIntEvaluator get(DriverContext context) {
      return new MvMinIntEvaluator(field.get(context), context);
    }

    @Override
    public String toString() {
      return "MvMin[field=" + field + "]";
    }
  }
}
