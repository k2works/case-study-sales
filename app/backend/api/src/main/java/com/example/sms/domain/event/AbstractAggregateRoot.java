package com.example.sms.domain.event;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.DomainEvents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 集約ルートの基本クラスです。
 */
public class AbstractAggregateRoot<A extends AbstractAggregateRoot<A>> extends AbstractEntity {
    @Transient
    private final List<Object> domainEvents = new ArrayList<>();

    /*
     * ドメインイベントを登録します。
     */
    protected <T> void registerEvent(T event) {
        this.domainEvents.add(event);
    }

    /**
     * ドメインイベントのコレクションを取得します。
     */
    @DomainEvents
    protected Collection<Object> domainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }
}
