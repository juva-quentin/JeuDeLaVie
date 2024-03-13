package org.quentin.gameoflife.controller;

import org.quentin.gameoflife.model.ChatMessage;
import org.quentin.gameoflife.service.JeuDeLaVieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Set;

@Controller
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private JeuDeLaVieService jeuDeLaVieService;

    @Autowired
    private SimpMessagingTemplate template;

    private Set<Integer> proposedSurviveRules = new HashSet<>();
    private Set<Integer> proposedBirthRules = new HashSet<>();

    @MessageMapping("/chat.sendMessage")
    public void receiveChatMessage(@Payload ChatMessage chatMessage) {
        if (chatMessage.getContent().startsWith("/proposeRules")) {
            handleRuleProposal(chatMessage.getContent());
            template.convertAndSend("/topic/chat", new ChatMessage(ChatMessage.MessageType.CHAT, "Nouvelles règles proposées. Tapez '/approveRules' pour les accepter.", "SYSTEM"));
        } else if (chatMessage.getContent().startsWith("/approveRules")) {
            handleRuleApproval();
            template.convertAndSend("/topic/chat", new ChatMessage(ChatMessage.MessageType.CHAT, "Les règles du jeu ont été mises à jour.", "SYSTEM"));
        } else {
            template.convertAndSend("/topic/chat", chatMessage);
        }
    }

    private void handleRuleProposal(String proposal) {

        String[] parts = proposal.split(" ");
        proposedSurviveRules.clear();
        proposedBirthRules.clear();
        boolean settingSurvive = false, settingBirth = false;
        for (String part : parts) {
            try {
                if (part.equalsIgnoreCase("survive")) {
                    settingSurvive = true;
                    settingBirth = false;
                } else if (part.equalsIgnoreCase("birth")) {
                    settingBirth = true;
                    settingSurvive = false;
                } else if (settingSurvive) {
                    proposedSurviveRules.add(Integer.parseInt(part));
                } else if (settingBirth) {
                    proposedBirthRules.add(Integer.parseInt(part));
                }
            } catch (NumberFormatException e) {
                logger.error("Erreur lors de la lecture des propositions de règles", e);
            }
        }
    }

    private void handleRuleApproval() {
        jeuDeLaVieService.setReglesSurvie(proposedSurviveRules);
        jeuDeLaVieService.setReglesNaissance(proposedBirthRules);
        proposedSurviveRules.clear();
        proposedBirthRules.clear();
    }
}
