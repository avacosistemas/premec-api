package ar.com.avaco.ws.service;

import java.util.List;

import ar.com.avaco.ws.dto.RepuestoDepositoDTO;

public interface RepuestoEPService {

	List<RepuestoDepositoDTO> getRepuestos(String username) throws Exception;

}
