package mixter.domain.core.message.handlers;

import mixter.domain.core.message.TimelineMessageProjection;
import mixter.domain.core.message.TimelineMessageRepository;
import mixter.domain.core.message.events.MessageQuacked;

public class UpdateTimeline {
    private final TimelineMessageRepository repository;

    public UpdateTimeline(TimelineMessageRepository timelineRepository) {
        this.repository = timelineRepository;
    }

    public void apply(MessageQuacked messageQuacked) {
        repository.save(new TimelineMessageProjection(
                messageQuacked.getAuthorId(),
                messageQuacked.getAuthorId(),
                messageQuacked.getMessage(),
                messageQuacked.getMessageId()));
    }
}
