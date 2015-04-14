package mixter.domain.identity.handlers;

import mixter.domain.identity.SessionProjection;
import org.assertj.core.util.Sets;

import java.util.Set;

public class FakeSessionProjectionRepository implements SessionProjectionRepository {

    private Set<SessionProjection> sessions = Sets.newHashSet();

    public Set<SessionProjection> getSessions() {
        return sessions;
    }
}
