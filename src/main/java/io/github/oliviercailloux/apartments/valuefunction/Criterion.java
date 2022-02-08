package io.github.oliviercailloux.apartments.valuefunction;

public enum Criterion {
  TELE(ValueFunctionType.IS_BOOLEAN_CRESCENT), TERRACE(ValueFunctionType.IS_BOOLEAN_CRESCENT), WIFI(
      ValueFunctionType.IS_BOOLEAN_CRESCENT), FLOOR_AREA(
          ValueFunctionType.IS_DOUBLE_CRESCENT), FLOOR_AREA_TERRACE(
              ValueFunctionType.IS_DOUBLE_CRESCENT), NB_BATHROOMS(
                  ValueFunctionType.IS_DOUBLE_CRESCENT), NB_BEDROOMS(
                      ValueFunctionType.IS_DOUBLE_CRESCENT), NB_SLEEPING(
                          ValueFunctionType.IS_DOUBLE_CRESCENT), NB_MIN_NIGHT(
                              ValueFunctionType.IS_DOUBLE_DECREASE), PRICE_PER_NIGHT(
                                  ValueFunctionType.IS_DOUBLE_DECREASE);

  private ValueFunctionType valueFunctionType;

  Criterion(ValueFunctionType valueFunctionType) {
    this.valueFunctionType = valueFunctionType;
  }

  /**
   * Used to retrieve the type associated with a <code>Criterion</code>
   *
   * @return the type <code>ValueFunctionType</code> associated with the criterion.
   */
  public ValueFunctionType getValueFunctionType() {
    return this.valueFunctionType;
  }

  /**
   * Indicates if the Criterion is boolean (ascending or descending)
   *
   * @return true if the Criterion is boolean increasing or decreasing, false otherwise
   */
  public boolean hasBooleanDomain() {
    return this.valueFunctionType.equals(ValueFunctionType.IS_BOOLEAN_CRESCENT)
        || this.valueFunctionType.equals(ValueFunctionType.IS_BOOLEAN_DECREASE);
  }

  /**
   * Indicates if the Criterion is double (ascending or descending)
   *
   * @return true if the Criterion is double increasing or decreasing, false otherwise
   */
  public boolean hasDoubleDomain() {
    return this.valueFunctionType.equals(ValueFunctionType.IS_DOUBLE_CRESCENT)
        || this.valueFunctionType.equals(ValueFunctionType.IS_DOUBLE_DECREASE);
  }

  /**
   * Indicates if the Criterion is equal to the criterion <code>vft</code> passed in parameter
   *
   * @param vft Comparison Criterion
   * @return true if the Criterion <code>vft</code> is equal to this Criterion, false otherwise
   */
  public boolean hasValueFunctionTypeEqualsTo(ValueFunctionType vft) {
    return this.valueFunctionType.equals(vft);
  }

  /**
   * Lets you know if the Criterion is an IS_DOUBLE_CRESCENT
   *
   * @return true if the Criterion is IS_DOUBLE_CRESCENT, false otherwise
   */
  public boolean isDoubleCrescent() {
    return this.valueFunctionType.equals(ValueFunctionType.IS_DOUBLE_CRESCENT);
  }

  /**
   * Lets you know if the Criterion is an IS_DOUBLE_DECREASE
   *
   * @return true if the Criterion is IS_DOUBLE_DECREASE, false otherwise
   */
  public boolean isDoubleDecrease() {
    return this.valueFunctionType.equals(ValueFunctionType.IS_DOUBLE_DECREASE);
  }

  /**
   * Lets you know if the Criterion is an IS_BOOLEAN_CRESCENT
   *
   * @return true if the Criterion is IS_BOOLEAN_CRESCENT, false otherwise
   */
  public boolean isBooleanCrescent() {
    return this.valueFunctionType.equals(ValueFunctionType.IS_BOOLEAN_CRESCENT);
  }

  /**
   * Lets you know if the Criterion is an IS_BOOLEAN_DECREASE
   *
   * @return true if the Criterion is IS_BOOLEAN_DECREASE, false otherwise
   */
  public boolean isBooleanDecrease() {
    return this.valueFunctionType.equals(ValueFunctionType.IS_BOOLEAN_DECREASE);
  }
}
