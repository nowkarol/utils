package com.gryglicki.nulls;

class Structure {
  private Second other;

  Structure(Second second) {
    this.other = second;
  }

  Second getSecond() {
    return other;
  }
}
