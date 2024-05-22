

Insert into FINANCING (invoice_id, purchaser_id, date_fullfilment, term, rate, early_payment_amount)
with financing_properties as (select INVOICE.ID                      as INVOICE_ID,
                                     CREDITOR.MAX_FINANCING_RATE_IN_BPS,
                                     PURCHASER.ID                    as PURCHASER_ID,
                                     PURCHASER.MINIMUM_FINANCING_TERM_IN_DAYS,
                                     DATEDIFF('DAY', CURRENT_DATE, INVOICE.MATURITY_DATE) as financingTerm,
                                     DATEDIFF('DAY', CURRENT_DATE, INVOICE.MATURITY_DATE) * PURCHASER_FINANCING_SETTINGS.ANNUAL_RATE_IN_BPS / 365 as financingRate,
                                     INVOICE.VALUE_IN_CENTS
                              from INVOICE
                                       join CREDITOR on INVOICE.CREDITOR_ID = CREDITOR.ID
                                       join PURCHASER_FINANCING_SETTINGS
                                            on PURCHASER_FINANCING_SETTINGS.CREDITOR_ID = CREDITOR.ID
                                       join PURCHASER_PURCHASER_FINANCING_SETTINGS
                                            on PURCHASER_PURCHASER_FINANCING_SETTINGS.PURCHASER_FINANCING_SETTINGS_ID =
                                               PURCHASER_FINANCING_SETTINGS.ID
                                       join PURCHASER
                                            on PURCHASER_PURCHASER_FINANCING_SETTINGS.PURCHASER_ID = PURCHASER.ID
                              where INVOICE.ID not in (select INVOICE_ID from FINANCING)),
     best_purchaser as (select INVOICE_ID,
                               PURCHASER_ID,
                               financingTerm,
                               financingRate,
                               VALUE_IN_CENTS,
                               ROW_NUMBER() over(partition by INVOICE_ID, PURCHASER_ID
                               order by financingRate) as rank
                        from financing_properties
                        where financingTerm >= MINIMUM_FINANCING_TERM_IN_DAYS
                          and financingRate < MAX_FINANCING_RATE_IN_BPS)
select INVOICE_ID,PURCHASER_ID,CURRENT_DATE,financingTerm,financingRate,VALUE_IN_CENTS*(1-financingRate/10000) from best_purchaser where best_purchaser.rank = 1