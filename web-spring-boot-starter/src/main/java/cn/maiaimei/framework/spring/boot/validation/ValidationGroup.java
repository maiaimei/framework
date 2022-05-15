package cn.maiaimei.framework.spring.boot.validation;

import javax.validation.GroupSequence;

public class ValidationGroup {
    public interface Create {
    }

    public interface Update {
    }

    @GroupSequence({OrderA.class, OrderB.class, OrderC.class, OrderD.class, OrderE.class,
            OrderF.class, OrderG.class, OrderH.class, OrderI.class, OrderJ.class})
    public interface CheckInOrder {
    }

    public interface OrderA {
    }

    public interface OrderB {
    }

    public interface OrderC {
    }

    public interface OrderD {
    }

    public interface OrderE {
    }

    public interface OrderF {
    }

    public interface OrderG {
    }

    public interface OrderH {
    }

    public interface OrderI {
    }

    public interface OrderJ {
    }
}
