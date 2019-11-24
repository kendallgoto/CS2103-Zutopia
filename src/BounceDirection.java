/**
 * A BounceDirection specifies to the Ball class whether a bounce should result in a negative, unchanged, positive, or negated velocity from the initial state.
 */
public enum BounceDirection {
    NEGATIVE, //Ensure the velocity is negative
    NOCHANGE, //Do not change this velocity
    POSITIVE, //Ensure the velocity is positive
    INVERSE   //regardless of initial velocity, inverse it
}
