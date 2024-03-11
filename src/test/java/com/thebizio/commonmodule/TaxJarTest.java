package com.thebizio.commonmodule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxjar.Taxjar;
import com.taxjar.exception.TaxjarException;
import com.taxjar.model.transactions.OrderResponse;
import com.thebizio.commonmodule.config.TaxjarConfig;
import com.thebizio.commonmodule.dto.BillingAddress;
import com.thebizio.commonmodule.exception.InvalidAddressException;
import com.thebizio.commonmodule.exception.TaxCalculationException;
import com.thebizio.commonmodule.exception.TaxSubmissionException;
import com.thebizio.commonmodule.service.tax.TaxJarService;
import junit.framework.TestCase;
import org.junit.Assert;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertThrows;


public class TaxJarTest extends TestCase {
    private TaxjarConfig taxjarConfig = new TaxjarConfig(System.getenv("TAXJAR_API_TOKEN"), "2022-01-24");

    // uncomment @Test & add `TAXJAR_API_TOKEN` in env
    // @Test
    public void testApp() throws TaxCalculationException, JsonProcessingException, InvalidAddressException, TaxSubmissionException, TaxjarException {

        Taxjar taxjar = taxjarConfig.taxjar();

        TaxJarService taxJarService = new TaxJarService(
                taxjar,
                new ObjectMapper(),
                new ModelMapper(),
                "SW053000"
        );

        // testing for address validation
        BillingAddress ba = new BillingAddress();

        assertThrows(InvalidAddressException.class, () -> taxJarService.getAddress(ba));

        ba.setCountry("US");
        ba.setZipcode("32801");
        ba.setState("FL");
        ba.setCity("Orlando");

        ba.setAddressLine1("incorrect line1");      // incorrect

        assertThrows(InvalidAddressException.class, () -> taxJarService.getAddress(ba));

        // correct line1
        ba.setAddressLine1("200 S Orange Ave");

        assertNotNull(taxJarService.getAddress(ba));

        // testing for tax calculation
        Assert.assertNotNull(taxJarService.calculateTax(ba, BigDecimal.valueOf(15), BigDecimal.valueOf(1.5)));

        System.out.println(taxJarService.calculateTax(ba, BigDecimal.valueOf(15), BigDecimal.valueOf(1.5)));
        System.out.println(taxJarService.calculateTax(ba, BigDecimal.valueOf(15), BigDecimal.valueOf(0)));

        assertTrue(taxJarService.calculateTax(ba, BigDecimal.valueOf(15000), BigDecimal.valueOf(1.5)).getTax() > 0.0);

        // test with full discount
        assertEquals(0.0, taxJarService.calculateTax(ba, BigDecimal.valueOf(15), BigDecimal.valueOf(15))
                .getTax());

        // with incorrect address
        final BillingAddress ba2 = new BillingAddress();
        assertThrows(TaxCalculationException.class, () -> taxJarService.calculateTax(ba2, BigDecimal.valueOf(15), BigDecimal.valueOf(1.5)));

        final BillingAddress ba3 = new BillingAddress();
        ba3.setCountry("US");
        ba3.setZipcode("85297");
        ba3.setState("AZ");
        ba3.setCity("Gilbert");
        ba3.setAddressLine1("incorrect");     // incorrect line1

        assertThrows(TaxCalculationException.class, () -> taxJarService.calculateTax(ba3, BigDecimal.valueOf(15), BigDecimal.valueOf(1.5)));

        String txId = UUID.randomUUID().toString();

        // submit tax test
        taxJarService.submitTax(ba, BigDecimal.valueOf(15.0), BigDecimal.valueOf(1.5), BigDecimal.valueOf(1.28), txId, "Sample Test", UUID.randomUUID().toString(), UUID.randomUUID().toString());

        // reading order submitted
        OrderResponse orderResponse = taxjar.showOrder(txId);

        assertEquals(txId, orderResponse.order.getTransactionId());
        assertEquals("13.5", orderResponse.order.getAmount().toString());
        assertEquals("1.5", orderResponse.order.getLineItems().get(0).getDiscount().toString());
        assertEquals("15.0", orderResponse.order.getLineItems().get(0).getUnitPrice().toString());
        assertEquals("1.28", orderResponse.order.getLineItems().get(0).getSalesTax().toString());
        assertEquals("1.28", orderResponse.order.getSalesTax().toString());
    }
}
