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
        eventPublisher.publish(new FolloweeMessageQuacked(projection.id, messageId));
    }

    private class DecisionProjection extends DecisionProjectionBase{
        private SubscriptionId id;

        public DecisionProjection(List<Event> history) {
            this.register(UserFollowed.class, this::apply);
            history.forEach(this::apply);
        }

        public void apply(UserFollowed even) {
            this.id = even.getSubscriptionId();
        }

        public SubscriptionId getId() {
            return id;
        }
    }
}
