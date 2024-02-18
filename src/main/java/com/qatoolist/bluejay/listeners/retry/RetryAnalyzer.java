package com.qatoolist.bluejay.listeners.retry;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer provides a mechanism to retry TestNG tests annotated with the custom @Retry annotation.
 * It takes into account the maxRetries and backoffTimeMs set on the annotation.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;

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
                    System.err.println("Thread interrupted during retry backoff: " + e.getMessage());
                }
                return true; // Trigger a retry
            }
        }
        return false; // No retries left, or method isn't annotated with @Retry
    }
}
