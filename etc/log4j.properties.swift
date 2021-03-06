# Set root category priority to WARN and its appenders to CONSOLE and FILE.
log4j.rootCategory=INFO, CONSOLE, FILE

log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout
log4j.appender.CONSOLE.Threshold=WARN
log4j.appender.CONSOLE.layout.ConversionPattern=%m%n

log4j.appender.FILE=org.swift.util.logging.LazyFileAppender
log4j.appender.FILE.File=swift.log
log4j.appender.FILE.layout=org.apache.log4j.PatternLayout
log4j.appender.FILE.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSSZZZZZ} %-5p %c{1} %m%n


log4j.logger.org.apache.axis.utils=ERROR

# Swift

log4j.logger.swift=DEBUG
log4j.logger.swift.textfiles=DEBUG
log4j.logger.org.globus.swift.trace=INFO
log4j.logger.org.griphyn.vdl.karajan.Loader=DEBUG
log4j.logger.org.griphyn.vdl.karajan.functions.ProcessBulkErrors=WARN
log4j.logger.org.griphyn.vdl.engine.Karajan=INFO
log4j.logger.org.griphyn.vdl.karajan.lib=INFO
log4j.logger.org.griphyn.vdl.karajan.VDL2ExecutionContext=DEBUG
log4j.logger.org.griphyn.vdl.karajan.lib.GetFieldValue=DEBUG
log4j.logger.org.griphyn.vdl.toolkit.VDLt2VDLx=DEBUG

log4j.logger.org.globus.cog.abstraction.coaster.service.job.manager.Cpu=DEBUG
log4j.logger.org.globus.cog.abstraction.coaster.service.job.manager.Block=DEBUG
log4j.logger.org.globus.cog.abstraction.coaster.service.job.manager.SimpleCloudWorkerManager=INFO


# Special functionality: suppresses auto-deletion of PBS submit file
log4j.logger.org.globus.cog.abstraction.impl.scheduler.common.AbstractExecutor=DEBUG
log4j.logger.org.globus.cog.abstraction.impl.scheduler.pbs.PBSExecutor=DEBUG

# CoG Karajan
log4j.logger.org.globus.cog.karajan.workflow.events.WorkerSweeper=WARN
log4j.logger.org.globus.cog.karajan.workflow.nodes.FlowNode=WARN

# CoG Scheduling
log4j.logger.org.globus.cog.karajan.scheduler.WeightedHostScoreScheduler=INFO

# CoG Providers
log4j.logger.org.globus.cog.abstraction.impl.common.task.TaskImpl=INFO
log4j.logger.org.globus.cog.abstraction.coaster.rlog=INFO
log4j.logger.com.parallelworks.impl.execution.ec2=DEBUG