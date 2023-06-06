package com.epam.ld.module2.testing.extension;


import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class CustomFilterExtension implements ExecutionCondition {

  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    var tags = context.getTags();
    if (tags.contains("fast")) {
      return ConditionEvaluationResult.disabled("fast tests are not run");
    } else {
      return ConditionEvaluationResult.enabled("normal test should run ");
    }
  }
}
