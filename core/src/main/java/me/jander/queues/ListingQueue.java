package me.jander.queues;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
public class ListingQueue {
    private static final String LISTING_QUEUE = "listings";

    private final AmazonSQS queue = AmazonSQSClientBuilder.defaultClient();

    private String queueURL = null;


    public SendMessageResult enqueueListing(ListingQueueRequest request)
        throws JsonProcessingException {
        ensureQueueURL();

        val msg = new ObjectMapper().writeValueAsString(request);
        SendMessageResult sendMessageResult = queue.sendMessage(queueURL, msg);

        return sendMessageResult;
    }

    /**
     * Ensures that a queue has been created; will create one if not present
     * @return true if queue already existed, false if it was created by this invocation
     */
    synchronized private boolean ensureQueueURL() {
        if (queueURL == null) {
            ListQueuesResult result = queue.listQueues();
            this.queueURL = result.getQueueUrls().stream().filter(url -> url.startsWith(LISTING_QUEUE)).findFirst().orElse(null);
            if (queueURL == null) {
                System.out.print("Creating new queue");
                val createResult = queue.createQueue(LISTING_QUEUE);
                queueURL = createResult.getQueueUrl();
                return false;
            }
        }

        return true;
    }
}
