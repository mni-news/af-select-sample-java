package com.alphaflash.select.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 *
 * Example JSON
 *         {
 *             "dataSeriesId": 205,
 *             "display": "MNI Chicago PMI",
 *             "previous": "58.2",
 *             "forecasts": [
 *                 {
 *                     "source": "MNI",
 *                     "value": "56.5"
 *                 }
 *             ],
 *             "actual": null,
 *             "type": "",
 *             "scale": ""
 *         }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSeriesEntry {
}
