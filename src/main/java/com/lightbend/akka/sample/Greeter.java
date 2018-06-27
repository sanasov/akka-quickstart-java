package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.lightbend.akka.sample.Printer.Greeting;

//#greeter-messages
public class Greeter extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    //#greeter-messages
    static public Props props(String message, ActorRef printerActor) {
        return Props.create(Greeter.class, () -> new Greeter(message, printerActor));
    }

    //#greeter-messages
    static public class WhoToGreet {
        public final String who;

        public WhoToGreet(String who) {
            this.who = who;
        }
    }

    static public class Greet {
        public Greet() {
        }
    }
    //#greeter-messages

    private final String message;
    private final ActorRef printerActor;
    private String greeting = "";

    public Greeter(String message, ActorRef printerActor) {
        this.message = message;
        this.printerActor = printerActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(WhoToGreet.class, wtg -> this.greeting = message + "; Who: " + wtg.who)
                .match(Greet.class,
                        x -> {
                            log.info("Greeter: " + greeting);
                            printerActor.tell(new Greeting("HELLO"), getSelf());
                        })
                .build();
    }
//#greeter-messages
}
//#greeter-messages
