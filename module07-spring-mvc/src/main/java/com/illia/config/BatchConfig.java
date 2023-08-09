package com.illia.config;

import com.illia.model.Ticket;
import com.illia.service.TicketService;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

  @Bean
  public Job preloadTicketsJob(JobRepository jobRepository, @Qualifier("preloadStep") Step step) {
    return new JobBuilder("preloadTicketsJob", jobRepository)
        .preventRestart()
        .start(step)
        .build();
  }

  @Bean
  protected Step preloadStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<Ticket> reader,
      ItemProcessor<Ticket, Ticket> processor,
      ItemWriter<Ticket> writer) {

    return new StepBuilder("preloadStep", jobRepository)
        .<Ticket, Ticket>chunk(10, transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public ItemReader<Ticket> ticketItemReader(Jaxb2Marshaller jaxb2Marshaller,
      @Value("${preload.tickets.file.path}") String path) {
    StaxEventItemReader<Ticket> reader = new StaxEventItemReader<>();
    reader.setResource(new ClassPathResource(path));
    reader.setFragmentRootElementName("ticket");
    reader.setUnmarshaller(jaxb2Marshaller);
    return reader;
  }

  @Bean
  public Jaxb2Marshaller ticketMarshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setPackagesToScan("com.illia.model");
    return marshaller;
  }

  @Bean
  public ItemProcessor<Ticket, Ticket> ticketItemProcessor() {
    return ticket -> ticket;
  }

  @Bean
  public ItemWriter<Ticket> ticketItemWriter(TicketService ticketService) {
    return tickets -> {
      for (Ticket ticket : tickets) {
        ticketService.bookTicket(ticket.getUserId(),
            ticket.getEventId(),
            ticket.getPlace(),
            ticket.getCategory());
      }
    };
  }

  @Bean
  public DataSource dataSource() {
    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    return builder.setType(EmbeddedDatabaseType.H2)
        .addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
        .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
        .build();
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    return new ResourcelessTransactionManager();
  }

  @Bean
  public JobRepository jobRepository(DataSource dataSource,
      PlatformTransactionManager transactionManager) throws Exception {
    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    factory.setDataSource(dataSource);
    factory.setTransactionManager(transactionManager);
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
    TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
    jobLauncher.setJobRepository(jobRepository);
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }

}
