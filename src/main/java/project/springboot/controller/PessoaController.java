package project.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import project.springboot.model.Pessoa;
import project.springboot.model.Telefone;
import project.springboot.repository.PessoaRepository;
import project.springboot.repository.TelefoneRepository;

@Controller
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@RequestMapping(value = "/cadastropessoa", method = RequestMethod.GET)
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		//retorna objeto vazio para o thymeleaf do form
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		//carregar lista de pessoas
		Iterable<Pessoa> pessoasLista = pessoaRepository.findAll();
		modelAndView.addObject("pessoas", pessoasLista);
		
		return modelAndView;
	}
	
	//ignorar tudo antes de /salvar
	@RequestMapping(value = "**/salvar", method = RequestMethod.POST)
	public ModelAndView salvar(Pessoa pessoa) {
		pessoaRepository.save(pessoa);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasLista = pessoaRepository.findAll();
		modelAndView.addObject("pessoas", pessoasLista);
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/listarpessoas", method = RequestMethod.GET)
	public ModelAndView listarPessoas() {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasLista = pessoaRepository.findAll();
		modelAndView.addObject("pessoas", pessoasLista);
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		return modelAndView;
	}
	
	@GetMapping(value = "/editarpessoa/{idpessoa}")
	public ModelAndView editarPessoa(@PathVariable("idpessoa") Long idpessoa) {
		Optional<Pessoa> pessoas = pessoaRepository.findById(idpessoa);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoaobj", pessoas.get());
		
		return modelAndView;
	}
	
	@GetMapping(value = "/removerpessoa/{idpessoa}")
	public ModelAndView removerPessoa(@PathVariable("idpessoa") Long idpessoa) {
		pessoaRepository.deleteById(idpessoa);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findAll());
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		return modelAndView;
	}
	
	@PostMapping(value = "/pesquisarpessoa")
	public ModelAndView pesquisarPessoa(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findByName(nomepesquisa));
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		return modelAndView;
	}
	
	@GetMapping(value = "/telefones/{idpessoa}")
	public ModelAndView mostrarTelefones(@PathVariable("idpessoa") Long idpessoa) {
		Optional<Pessoa> pessoas = pessoaRepository.findById(idpessoa);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj", pessoas.get());
		modelAndView.addObject("telefones", telefoneRepository.findTelefones(idpessoa));
		
		return modelAndView;
	}
	
	@PostMapping(value = "**/addtelefone/{pessoaid}")
	public ModelAndView addTelefone(Telefone telefone, @PathVariable("pessoaid") Long pessoaid) {
		Pessoa pessoa = pessoaRepository.findById(pessoaid).get();
		telefone.setPessoa(pessoa);
		
		telefoneRepository.save(telefone);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.findTelefones(pessoaid));
		
		return modelAndView;
	}
	
	@GetMapping(value = "/removertelefone/{idtelefone}")
	public ModelAndView removerTelefone(@PathVariable("idtelefone") Long idtelefone) {
		Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();
		
		telefoneRepository.deleteById(idtelefone);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.findTelefones(pessoa.getId()));
		
		return modelAndView;
	}
}