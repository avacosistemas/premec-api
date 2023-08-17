package ar.com.avaco.entities;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import ar.com.avaco.ws.service.FormularioEPService;

@Service("jobEnvioFormularioSap")
public class JobEnvioFormularioSap implements Job {

	private FormularioEPService formularioEPService;

	private static final Logger LOGGER = Logger.getLogger(JobEnvioFormularioSap.class); 
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.debug("Iniciando proceso de envio de formularios a SAP");
		formularioEPService = (FormularioEPService) context.getJobDetail().getJobDataMap().get("formularioEPService");
		try {
			formularioEPService.enviarFormulariosFromFiles();
		} catch (Exception e) {
			LOGGER.error("Ocurrio un error en el proceso de envio de formularios a SAP");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		LOGGER.debug("Finalizado proceso de envio de formularios a SAP");
	}

	@Resource(name = "formularioEPService")
	public void setFormularioEPService(FormularioEPService formularioEPService) {
		this.formularioEPService = formularioEPService;
	}

}
