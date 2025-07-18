package ar.com.avaco.ws.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.factory.SapBusinessException;
import ar.com.avaco.ws.dto.attachment.ResponseAttachmentGetPost;
import ar.com.avaco.ws.service.AbstractSapService;

@Service("attachmentService")
public class AttachmentServiceImpl extends AbstractSapService implements AttachmentService {

	@Override
	public ResponseAttachmentGetPost getAttachment(Long attachmentEntry) {
		String attachmentGetUrl = urlSAP + "/Attachments2(" + attachmentEntry + ")";

		ResponseEntity<ResponseAttachmentGetPost> currentAttachmentResponse;
		try {
			currentAttachmentResponse = getRestTemplate().doExchange(attachmentGetUrl, HttpMethod.GET, null,
					ResponseAttachmentGetPost.class);
		} catch (SapBusinessException e) {
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("url", attachmentGetUrl);
			errors.put("error", e.getMessage());
			e.printStackTrace();
			throw new ErrorValidationException("Error al ejecutar el siguiente WS", errors);
		}

		ResponseAttachmentGetPost currentAttach = currentAttachmentResponse.getBody();
		return currentAttach;
	}
	
	@Override
	public Long enviarAttachmentsSap(List<Map<String, String>> attachments) {
		Map<String, Object> attachmentMap = new HashMap<>();
		attachmentMap.put("Attachments2_Lines", attachments.toArray());

		// Preparo la url para enviar el attachment
		String attachmentUrl = urlSAP + "/Attachments2";
		HttpEntity<Map<String, Object>> httpEntityAttach = new HttpEntity<>(attachmentMap);
		ResponseEntity<ResponseAttachmentGetPost> attachmentRespose = null;

		try {
			attachmentRespose = getRestTemplate().doExchange(attachmentUrl, HttpMethod.POST, httpEntityAttach,
					ResponseAttachmentGetPost.class);
		} catch (SapBusinessException e) {
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("url", attachmentUrl);
			errors.put("error", e.getMessage());
			e.printStackTrace();
			throw new ErrorValidationException("Error al ejecutar el siguiente WS", errors);
		}

		return attachmentRespose.getBody().getAbsoluteEntry();

	}
	
}
