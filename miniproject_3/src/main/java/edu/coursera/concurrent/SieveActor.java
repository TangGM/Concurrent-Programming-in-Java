package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;

import static edu.rice.pcdp.PCDP.finish;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 */
public final class SieveActor extends Sieve {


    /**
     * {@inheritDoc}
     */
    @Override
    public int countPrimes(final int limit) {
        final SieveActorActor sieveActorActor = new SieveActorActor(3);
        finish(() -> {
            for (int i = 4; i < limit + 1; i++) {
                if (i % 2 == 1) {
                    sieveActorActor.send(i);
                }
            }
        });
        SieveActorActor loopActor = sieveActorActor;
        int numPrimes = 1;
        while (loopActor != null) {
            loopActor = loopActor.nextActor;
            numPrimes++;
        }
        return numPrimes;
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {
        private int localPrime;
        private SieveActorActor nextActor;

        SieveActorActor (final int localPrime) {
            this.localPrime = localPrime;
        }
        /**
         * Process a single message sent to this actor.
         *
         * @param msg Received message
         */
        @Override
        public void process(final Object msg) {
            Integer candidate = (Integer) msg;
            if (candidate % localPrime != 0) {
                if (nextActor == null) {
                    nextActor = new SieveActorActor(candidate);
                } else {
                    nextActor.send(msg);
                }
            }
        }
    }
}
