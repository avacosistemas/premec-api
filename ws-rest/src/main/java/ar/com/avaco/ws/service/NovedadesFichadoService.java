package ar.com.avaco.ws.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.ws.rest.security.dto.RegistroFichadoDTO;

public interface NovedadesFichadoService {

	Map<Usuario, List<RegistroFichadoDTO>> parsearExcelNovedadesFichado() throws IOException;

}
