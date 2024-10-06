package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.Sugang;

public interface SugangRepository  {

    <S extends Sugang> S save(S item);
}
