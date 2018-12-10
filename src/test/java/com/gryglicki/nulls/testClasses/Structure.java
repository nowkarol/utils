package com.gryglicki.nulls.testClasses;

public class Structure {
  private Second other;

  public Structure(Second second) {
    this.other = second;
  }

  public Second getSecond() {
    return other;
  }
}
