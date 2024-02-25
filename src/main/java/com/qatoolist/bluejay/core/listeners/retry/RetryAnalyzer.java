package com.qatoolist.bluejay.core.listeners.retry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer provides a mechanism to retry TestNG tests annotated with the custom @Retry annotation.
 * It takes into account the maxRetries and backoffTimeMs set on the annotation.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;

    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);

    /**
     * Determines if a failed test should be retried based on the configured maximum retries and backoff time.
     *
     * @param result The result of the test method.
     * @return true if the test should be retried, false otherwise.
     */
    @Override
    public boolean retry(ITestResult result) {
        Retry retryAnnotation = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Retry.class);

        if (retryAnnotation != null) {
            int maxRetries = retryAnnotation.maxRetries();
            long backoffTimeMs = retryAnnotation.backoffTimeMs();

            if (retryCount < maxRetries) {
                retryCount++;
                try {
                    Thread.sleep(backoffTimeMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interrupted status
                    String error = "Thread interrupted during retry backoff: " + e.getMessage();
                    logger.error( error );
                }
                return true; // Trigger a retry
            }
        }
        return false; // No retries left, or method isn't annotated with @Retry
    }
}
