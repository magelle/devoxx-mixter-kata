package mixter.domain.core.subscription;

import mixter.doc.Aggregate;
import mixter.domain.DecisionProjectionBase;
import mixter.domain.Event;
import mixter.domain.EventPublisher;
import mixter.domain.core.message.MessageId;
import mixter.domain.core.subscription.events.FolloweeMessageQuacked;
import mixter.domain.core.subscription.events.UserFollowed;
import mixter.domain.core.subscription.events.UserUnfollowed;
import mixter.domain.identity.UserId;

import java.util.List;

@Aggregate
public class Subscription {

    private final DecisionProjection projection;

    public Subscription(List<Event> history) {
        projection = new DecisionProjection(history);
    }

    public static void follow(UserId follower, UserId followee, EventPublisher eventPublisher) {
        eventPublisher.publish(new UserFollowed(new SubscriptionId(follower, followee)));
    }

    public void unfollow(EventPublisher eventPublisher) {
        eventPublisher.publish(new UserUnfollowed(projection.getId()));
    }

    public void notifyFollower(MessageId messageId, EventPublisher eventPublisher) {
        if(! projection.isDisabled())
            eventPublisher.publish(new FolloweeMessageQuacked(projection.id, messageId));
    }

    private class DecisionProjection extends DecisionProjectionBase{
        private SubscriptionId id;
        private boolean disabled = false;

        public DecisionProjection(List<Event> history) {
            this.register(UserFollowed.class, this::apply);
            this.register(UserUnfollowed.class, this::apply);
            history.forEach(this::apply);
        }

        public void apply(UserFollowed even) {
            this.id = even.getSubscriptionId();
        }

        public void apply(UserUnfollowed even) {
            this.disabled = true;
        }

        public SubscriptionId getId() {
            return id;
        }

        public boolean isDisabled() {
            return disabled;
        }
    }
}
