# Read Me First

The original idea is from  
https://medium.com/@sehgal.mohit06/spring-batch-sequential-job-flow-demo-application-9527c710bfc6

# Tasklet vs Chunk

Unlike the chunk’s concept, the tasklet step assumes the responsibility to execute a unique task individually. There’s
no necessity to process the data in multiple batches. We can use, for example, a tasklet execution if we need to remove
duplicated registers from the database.
