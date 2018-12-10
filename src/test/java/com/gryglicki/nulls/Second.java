package com.gryglicki.nulls;

class Second {
  private Third third;
  private ThirdSubclass thirdSubclass;

  Second(Third third, ThirdSubclass thirdSubclass) {
    this.third = third;
    this.thirdSubclass = thirdSubclass;
  }

  public Second(Third third) {
    this(third, null);
  }

  Third getThird() {
    return third;
  }

  ThirdSubclass getThirdSubclass() {
    return thirdSubclass;
  }
}
