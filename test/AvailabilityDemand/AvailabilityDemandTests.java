
package AvailabilityDemand;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CSE 460 Software Analysis and Design Project - Sample Test Cases
 * This class contains some test cases that will be used in automated grading of your project.
 * Note that these test cases are not exhaustive
 *
 * These tests concern only with the correctness and robustness of your implementation. You are still required to
 * adopt Publisher-Subscriber pattern in your design and make use of good programming practises.
 *
 * The undisclosed part of the test will differ from this test somewhat:
 * - Test cases are automatically generated with random characters (other than commas and leading/trailing spaces).
 * - The generated test cases can be very long and contains unusual sequences.
 * - The generated test cases are based on criteria imposed in the project descriptions, which any implementation is
 *   bound to abide.
 * - Instead of testing the output of your program against a "standard answer", your output would be compared against
 *   the output of a "Reference Implementation" (RI), which is seen as the authoritative answer.
 *  - Could the RI be wrong? Yes, but unlikely. Contact us if you believe it is the case.
 * @version 1.0
 * Author: Sheetal Mohite <smohite3@asu.edu>
 */

public class AvailabilityDemandTests {
    private static AvailabilityDemand availabilityDemand;

    // Create AvailabilityDemand object to test with
    @BeforeClass
    public static void setupAvailabilityDemand() {
        availabilityDemand = new AvailabilityDemand();
    }

    // Reset the availabilityDemand object every time a test finishes so that it can accept a new batch of commands
    @After
    public void resetAvailabilityDemand() {
        availabilityDemand.reset();
    }

    @Test
    //@GradedTest(name = "1: Normal, Multiple combinations of published, subscribed, and unsubscribed -- case 1"
    public void testMultipleSubUnsubPublishForValidInputsCaseOne() {
        // Expected output
        List<String> expected = new ArrayList<>(Arrays.asList(
                "John Doe notified of B&B availability in New York City from 12/01/2021 to 12/05/2021 by High-Mountains B&B",
                "John Doe notified of B&B availability in New York City from 11/30/2021 to 12/09/2021 by AirClouds B&B",
                "Jane Doe notified of B&B availability in New York City from 11/30/2021 to 12/09/2021 by AirClouds B&B",
                "Richard notified of B&B availability in New York City from 12/01/2021 to 12/05/2021 by High-Mountains B&B",
                "Richard notified of B&B availability in New York City from 11/30/2021 to 12/09/2021 by AirClouds B&B",
                "William notified of B&B availability in New York City from 12/10/2021 to 12/15/2021 by AirClouds B&B"
        ));
        // Feed the availabilityDemand object with some commands
        availabilityDemand.processInput("publish, High-Mountains, New York City, 12/01/2021, 12/05/2021");
        availabilityDemand.processInput("publish, AirClouds, New York City, 11/30/2021, 12/09/2021");
        //john should get notified by both providers
        availabilityDemand.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
        //William will not get notified as no providers match the criteria
        availabilityDemand.processInput("subscribe, William, New York City, 12/10/2021, 12/15/2021");
        //jane should get notified by airclouds as he matches the criteria
        availabilityDemand.processInput("subscribe, Jane Doe, New York City, 12/06/2021, 12/09/2021");
        //one subscriber removed from system
        availabilityDemand.processInput("unsubscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
        //richard should get notified by both providers
        availabilityDemand.processInput("subscribe, Richard, New York City, 12/01/2021, 12/04/2021");
        //one subscriber removed from system
        availabilityDemand.processInput("unsubscribe, Jane Doe, New York City, 12/06/2021, 12/09/2021");
        //no notification as no one matches the criteria
        availabilityDemand.processInput("publish, High-Mountains, New York City, 12/06/2021, 12/10/2021");
        //only william will get the notification
        availabilityDemand.processInput("publish, AirClouds, New York City, 12/10/2021, 12/15/2021");

        // Obtain the actual result from your availabilityDemand object and compare it with the expected output
        List<String> actual = availabilityDemand.getAggregatedOutput().stream()
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        expected =  expected.stream().map(String :: toLowerCase).collect(Collectors.toList());

        assertEquals(expected, actual);
    }
    // Note: @GradedTest is for GradeScope scoring only and does not affect the test in any way.

    @Test
    //@GradedTest(name = "2: Abnormal, Published dates from the same provider for the same location cannot be the same"
    public void testDateConstraintsOnProvider() {
        // Expected output
        List<String> expected = new ArrayList<>(Arrays.asList(
                "John Doe notified of B&B availability in New York City from 11/30/2021 to 12/05/2021 by High-Mountains B&B",
                "Jane Doe notified of B&B availability in New York City from 12/10/2021 to 12/15/2021 by High-Mountains B&B"
        ));
        // Feed the availabilityDemand object with some commands
        availabilityDemand.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
        availabilityDemand.processInput("subscribe, Jane Doe, New York City, 12/10/2021, 12/15/2021");
        availabilityDemand.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/05/2021");
        //duplicate with the first published availability, should be discarded
        availabilityDemand.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/05/2021");

        availabilityDemand.processInput("publish, High-Mountains, New York City, 12/10/2021, 12/15/2021");
        // Obtain the actual result from your availabilityDemand object and compare it with the expected output
        List<String> actual = availabilityDemand.getAggregatedOutput().stream()
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        expected =  expected.stream().map(String :: toLowerCase).collect(Collectors.toList());

        assertEquals(expected, actual);
    }


    @Test
    public void customTest()
    {
        availabilityDemand.processInput("subscribe, John Doe, New York City, 12/11/2021, 12/14/2021");
        availabilityDemand.processInput("publish, 1, New York City, 12/10/2021, 12/15/2021");
        availabilityDemand.processInput("publish, 2, New York City, 12/10/2021, 12/15/2021");
        availabilityDemand.processInput("publish, 3, New York City, 12/10/2021, 12/15/2021");
        List<String> actual = availabilityDemand.getAggregatedOutput();
        System.out.println();

    }

    @Test
    //@GradedTest(name = "3: Abnormal, Same published dates from same provider but for different locations should be stored and treated as unique"
    public void testProvidersWithDifferentLocationToBeUnique() {
        // Expected output
        List<String> expected = new ArrayList<>(Arrays.asList(
                "John Doe notified of B&B availability in New York City from 11/30/2021 to 12/05/2021 by High-Mountains B&B",
                "Jane Doe notified of B&B availability in Tempe from 11/30/2021 to 12/05/2021 by High-Mountains B&B",
                "Jane Doe notified of B&B availability in Seattle from 12/01/2021 to 12/10/2021 by High-Mountains B&B"
        ));
        // Feed the availabilityDemand object with some commands
        availabilityDemand.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
        availabilityDemand.processInput("subscribe, Jane Doe, Tempe, 11/30/2021, 12/05/2021");
        availabilityDemand.processInput("subscribe, Jane Doe, Seattle, 12/01/2021, 12/10/2021");

        availabilityDemand.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/05/2021");

        availabilityDemand.processInput("publish, High-Mountains, Tempe, 11/30/2021, 12/05/2021");

        availabilityDemand.processInput("publish, High-Mountains, Seattle, 12/01/2021, 12/10/2021");

        // Obtain the actual result from your availabilityDemand object and compare it with the expected output
        List<String> actual = availabilityDemand.getAggregatedOutput().stream()
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        expected =  expected.stream().map(String :: toLowerCase).collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    //@GradedTest(name = "4: Abnormal, Duplicate subscriptions should be ignored"
    public void testSubscriberWithDuplicateSubscriptionShouldBeIgnored() {
        // Expected output
        List<String> expected = new ArrayList<>(Arrays.asList(
                "John Doe notified of B&B availability in New York City from 11/30/2021 to 12/05/2021 by High-Mountains B&B",
                "John Doe notified of B&B availability in New York City from 12/01/2021 to 12/05/2021 by AirClouds B&B"
        ));
        // Feed the availabilityDemand object with some commands
        availabilityDemand.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/05/2021");
        availabilityDemand.processInput("publish, AirClouds, New York City, 12/01/2021, 12/05/2021");
        availabilityDemand.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
        availabilityDemand.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");

        // Obtain the actual result from your availabilityDemand object and compare it with the expected output
        List<String> actual = availabilityDemand.getAggregatedOutput().stream()
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        expected =  expected.stream().map(String :: toLowerCase).collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    //@GradedTest(name = "5: Normal, Subscriptions and publications with random order"
    public void testSubPubForValidInputs() {
        // Expected output
        List<String> expected = new ArrayList<>(Arrays.asList(
                "John Doe notified of B&B availability in New York City from 11/30/2021 to 12/15/2021 by High-Mountains B&B",
                "Jane Doe notified of B&B availability in Tempe from 11/28/2021 to 12/02/2021 by High-Mountains B&B",
                "Jane Doe notified of B&B availability in Tempe from 11/28/2021 to 12/05/2021 by AirCloud B&B"
        ));

        // Feed the availabilityDemand object with some commands
        availabilityDemand.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
        availabilityDemand.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/15/2021");
        availabilityDemand.processInput("subscribe, Jane Doe, Tempe, 11/29/2021, 12/02/2021");
        availabilityDemand.processInput("publish, High-Mountains, Tempe, 11/28/2021, 12/02/2021");
        availabilityDemand.processInput("publish, AirCloud, Tempe, 11/28/2021, 12/05/2021");

        // Obtain the actual result from your availabilityDemand object and compare it with the expected output
        List<String> actual = availabilityDemand.getAggregatedOutput().stream()
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        expected =  expected.stream().map(String :: toLowerCase).collect(Collectors.toList());
        assertEquals(expected, actual);
    }

    @Test
    //@GradedTest(name = "6: Abnormal, Same subscription and publication dates"
    public void testSameDatePublishedEventsShouldBeProvidedToCustomersSatisfyingTheCriteria() {
        // Expected output
        List<String> expected = new ArrayList<>(Arrays.asList(
                "John Doe notified of B&B availability in New York City from 12/01/2021 to 12/05/2021 by High-Mountains B&B",
                "William notified of B&B availability in New York City from 12/10/2021 to 12/15/2021 by AirClouds B&B"
        ));
        // Feed the availabilityDemand object with some commands
        availabilityDemand.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
        availabilityDemand.processInput("subscribe, William, New York City, 12/10/2021, 12/15/2021");
        // John Doe's subscription fits within the criteria thus, John Doe should get notified of this event
        availabilityDemand.processInput("publish, High-Mountains, New York City, 12/01/2021, 12/05/2021");
        // William's subscription fits within the criteria thus, William should get notified of this event
        availabilityDemand.processInput("publish, AirClouds, New York City, 12/10/2021, 12/15/2021");

        // Obtain the actual result from your availabilityDemand object and compare it with the expected output
        List<String> actual = availabilityDemand.getAggregatedOutput().stream()
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        expected =  expected.stream().map(String :: toLowerCase).collect(Collectors.toList());

        assertEquals(expected, actual);
    }
    @Test
    //@GradedTest(name = "7: Abnormal, Illegal parameters and date constraints"
    public void testIllegalParamLens() {

        //Date format invalid: DD/MM/YYYY
        availabilityDemand.processInput("subscribe, John Doe, New York City, 15/01/2022, 30/01/2022");
        //Date format invalid: DD MMM YYYY
        availabilityDemand.processInput("publish, High-Mountains, New York City, 14 Jan 2022, 30 Jan 2022");
        //stay period to date must be smaller than stay period from date
        availabilityDemand.processInput("subscribe, John Doe, New York City, 30/01/2022, 15/01/2022");
        //extra parameter in the publish method
        availabilityDemand.processInput("publish, High-Mountains, New York City, 14 Jan 2022, 30 Jan 2022, great view and lot of space");
        //available till date must be smaller than available from date
        availabilityDemand.processInput("publish, AirClouds, New York City, 30/01/2022, 15/01/2022");

        // Feed the availabilityDemand object with some commands
        availabilityDemand.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
        availabilityDemand.processInput("subscribe, William, New York City, 12/10/2021, 12/15/2021");
        // to date needs to be same as or greater than end date of subscribed period
        availabilityDemand.processInput("publish, High-Mountains, New York City, 11/29/2021, 12/02/2021");
        // start date of availability period needs to be later than default date which is 11/27/2021
        availabilityDemand.processInput("publish, High-Mountains, New York City, 11/20/2021, 12/05/2021");
        // start date of stay period needs to be later than default date which is 11/27/2021
        availabilityDemand.processInput("subscribe, Jane Doe, New York City, 11/20/2021, 12/05/2021");
        // Obtain the actual result from your availabilityDemand object and compare it with the expected output
        List<String> actual = availabilityDemand.getAggregatedOutput();
        // Expected output (nothing)
        assertEquals(0, actual.size());
    }
    @Test
    //@GradedTest(name = "8: Abnormal, Published dates from the same provider for the same location cannot overlap"
    public void testPublishDatesFromSameProviderCannotOverlap() {
        // Expected output
        List<String> expected = new ArrayList<>(Arrays.asList(
                "John Doe notified of B&B availability in New York City from 11/30/2021 to 12/05/2021 by High-Mountains B&B"
        ));
        // Feed the availabilityDemand object with some commands
        availabilityDemand.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
        availabilityDemand.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/05/2021");
        //overlaps with the first published availability, should be discarded
        availabilityDemand.processInput("publish, High-Mountains, New York City, 11/29/2021, 12/02/2021");
        //overlaps with the first published availability, should be discarded
        availabilityDemand.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/15/2021");
        // Obtain the actual result from your availabilityDemand object and compare it with the expected output
        List<String> actual = availabilityDemand.getAggregatedOutput().stream()
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        expected =  expected.stream().map(String :: toLowerCase).collect(Collectors.toList());

        assertEquals(expected, actual);
    }
    @Test
    //@GradedTest(name = "9: Normal, Provider, customer and location names should be case-insensitive"
    public void testSubPubForCaseInsensitiveInputs() {
        // Expected output
        List<String> expected = new ArrayList<>(Arrays.asList(
                "John Doe notified of B&B availability in New York City from 11/30/2021 to 12/15/2021 by High-Mountains B&B",
                "Jane Doe notified of B&B availability in Tempe from 11/28/2021 to 12/02/2021 by High-Mountains B&B",
                "Jane Doe notified of B&B availability in Tempe from 11/28/2021 to 12/05/2021 by AirCloud B&B"
        ));

        // Feed the availabilityDemand object with some commands
        availabilityDemand.processInput("subscribe, John Doe, new York City, 12/01/2021, 12/05/2021");
        availabilityDemand.processInput("publish, High-Mountains, New York CITY, 11/30/2021, 12/15/2021");
        availabilityDemand.processInput("subscribe, JANE DOE, tempe, 11/29/2021, 12/02/2021");
        availabilityDemand.processInput("publish, High-mountains, Tempe, 11/28/2021, 12/02/2021");
        availabilityDemand.processInput("publish, airCloud, TemPe, 11/28/2021, 12/05/2021");

        // Obtain the actual result from your availabilityDemand object and compare it with the expected output
        List<String> actual = availabilityDemand.getAggregatedOutput().stream()
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        expected =  expected.stream().map(String :: toLowerCase).collect(Collectors.toList());
        assertEquals(expected, actual);
    }

    @Test
    //@GradedTest(name = "10: Normal, Multiple published, subscribed and unsubscribed -- case 2"
    public void testMultiSubUnsubForValidInputsCaseTwo() {
        // Expected output
        List<String> expected = new ArrayList<>(Arrays.asList(
                "John Doe notified of B&B availability in New York City from 11/30/2021 to 12/15/2021 by High-Mountains B&B",
                "William notified of B&B availability in New York City from 11/30/2021 to 12/15/2021 by High-Mountains B&B",
                "William notified of B&B availability in New York City from 11/30/2021 to 12/15/2021 by High-Mountains B&B",
                "William notified of B&B availability in New York City from 11/30/2021 to 12/15/2021 by AirClouds B&B",
                "John Doe notified of B&B availability in New York City from 11/30/2021 to 12/15/2021 by High-Mountains B&B",
                "John Doe notified of B&B availability in New York City from 11/30/2021 to 12/15/2021 by AirClouds B&B"
        ));
        // Feed the availabilityDemand object with some commands
        availabilityDemand.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
        availabilityDemand.processInput("subscribe, William, New York City, 12/10/2021, 12/15/2021");
        //both subscribers should get the notification as satisfy the criteria
        availabilityDemand.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/15/2021");
        //one subscriber removed from system
        availabilityDemand.processInput("unsubscribe, William, New York City, 12/10/2021, 12/15/2021");
        //duplicate published event, no action taken
        availabilityDemand.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/15/2021");
        //one subscriber removed from system, no subscribers in system
        availabilityDemand.processInput("unsubscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
        //no subscribers in system, system will store the event
        availabilityDemand.processInput("publish, AirClouds, New York City, 11/30/2021, 12/15/2021");
        // both stored published events will be fired for below customer since this one comes as a new subscription
        availabilityDemand.processInput("subscribe, William, New York City, 12/10/2021, 12/15/2021");
        // both stored published events will be fired for below customer since this one comes as a new subscription
        availabilityDemand.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");

        // Obtain the actual result from your availabilityDemand object and compare it with the expected output
        List<String> actual = availabilityDemand.getAggregatedOutput().stream()
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        expected =  expected.stream().map(String :: toLowerCase).collect(Collectors.toList());

        assertEquals(expected, actual);
    }
}

