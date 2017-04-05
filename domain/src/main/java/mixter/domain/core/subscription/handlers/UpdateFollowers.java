package mixter.domain.core.subscription.handlers;

import mixter.doc.Handler;
import mixter.domain.Event;
import mixter.domain.core.subscription.FollowerRepository;
import mixter.domain.core.subscription.SubscriptionId;
import mixter.domain.core.subscription.events.UserFollowed;

@Handler
public class UpdateFollowers {

    private final FollowerRepository repository;

    public UpdateFollowers(FollowerRepository repository) {
        this.repository = repository;
    }

    public void apply(UserFollowed userFollowed) {
        SubscriptionId subscriptionId = userFollowed.getSubscriptionId();
        repository.saveFollower(subscriptionId.getFollowee(), subscriptionId.getFollower());
    }
}
