package org.linkedprocess.farm.os.errors;

/**
 * Author: josh
 * Date: Jun 30, 2009
 * Time: 3:44:43 PM
 */
public class JobNotFoundException extends SchedulerException {
    public JobNotFoundException(final String jobID) {
        super("job '" + jobID + "' does not exist");
    }
}
