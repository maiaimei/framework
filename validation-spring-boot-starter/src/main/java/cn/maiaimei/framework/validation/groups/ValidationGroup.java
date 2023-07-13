package cn.maiaimei.framework.validation.groups;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

public class ValidationGroup {

  public interface Insert extends Default {

  }

  public interface Update extends Default {

  }

  @GroupSequence({OrderA.class, OrderB.class, OrderC.class, OrderD.class, OrderE.class,
      OrderF.class, OrderG.class, OrderH.class, OrderI.class, OrderJ.class})
  public interface InOrder extends Default {

  }

  public interface OrderA extends Default {

  }

  public interface OrderB extends Default {

  }

  public interface OrderC extends Default {

  }

  public interface OrderD extends Default {

  }

  public interface OrderE extends Default {

  }

  public interface OrderF extends Default {

  }

  public interface OrderG extends Default {

  }

  public interface OrderH extends Default {

  }

  public interface OrderI extends Default {

  }

  public interface OrderJ extends Default {

  }
}
