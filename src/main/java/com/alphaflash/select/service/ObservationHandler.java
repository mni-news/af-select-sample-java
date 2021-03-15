package com.alphaflash.select.service;

import com.alphaflash.select.dto.Observation;

import java.util.Collection;

public interface ObservationHandler {

    void handleObservations(Collection<Observation> observations);
}
