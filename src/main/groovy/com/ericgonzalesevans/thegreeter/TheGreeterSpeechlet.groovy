package com.ericgonzalesevans.thegreeter

import com.amazon.speech.slu.Intent
import com.amazon.speech.speechlet.IntentRequest
import com.amazon.speech.speechlet.LaunchRequest
import com.amazon.speech.speechlet.Session
import com.amazon.speech.speechlet.SessionEndedRequest
import com.amazon.speech.speechlet.SessionStartedRequest
import com.amazon.speech.speechlet.Speechlet
import com.amazon.speech.speechlet.SpeechletException
import com.amazon.speech.speechlet.SpeechletResponse
import com.amazon.speech.ui.PlainTextOutputSpeech
import com.amazon.speech.ui.Reprompt
import com.amazon.speech.ui.SimpleCard

class TheGreeterSpeechlet implements Speechlet {

    private static final GREETINGS = [
        "Hello, there!",
        "Hi! How are you?",
        "Howdy!",
        "Hi.",
        "What's up?",
        "What's new?",
        "Yo!",
        "Hiya!",
        "Hey hey hey!",
        "Greetings!"
    ]

    void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
        // any initialization logic goes here
    }

    SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        return getNewGreetingResponse() //gives a greeting as soon as the app is launched
    }

    SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        def intent = request.getIntent()
        def intentName = (intent != null) ? intent.getName() : null

        switch (intentName) {
            case "GetNewGreetingIntent":
                return getNewGreetingResponse()
                break
            case "GreetUserIntent":
                return getNewUserGreetingResponse(intent)
                break
            case "AMAZON.HelpIntent":
                return getNewHelpResponse()
                break
            case "AMAZON.StopIntent":
                PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech()
                outputSpeech.setText("Goodbye")
                return SpeechletResponse.newTellResponse(outputSpeech)
                break
            case "AMAZON.CancelIntent":
                PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech()
                outputSpeech.setText("Goodbye")
                return SpeechletResponse.newTellResponse(outputSpeech)
                break
            default:
                throw new SpeechletException("Invalid Intent")
                break
        }
    }

    void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
        // any cleanup logic goes here
    }

    private static getNewGreetingResponse() {
        def greeting = GREETINGS.sort{ new Random() }?.take(1)?.first()

        // Create the Simple card content.
        def card = new SimpleCard()
        card.setTitle("TheGreeter")
        card.setContent(greeting)

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech()
        speech.setText(greeting)

        return SpeechletResponse.newTellResponse(speech, card)
    }

    private static getNewUserGreetingResponse(Intent intent) {
        def user = intent.getSlot("UserName").getValue()
        def greeting = GREETINGS.sort{ new Random() }?.take(1)?.first()

        greeting += " " + user + "!"

        // Create the Simple card content.
        def card = new SimpleCard()
        card.setTitle("TheGreeter")
        card.setContent(greeting)

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech()
        speech.setText(greeting)

        return SpeechletResponse.newTellResponse(speech, card)
    }

    private static getNewHelpResponse() {
        def helpText = "You can ask The Greeter to greet someone, or, you can say exit. What can I do for you?"
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech()
        speech.setText(helpText)

        // Create reprompt.
        Reprompt reprompt = new Reprompt()
        reprompt.setOutputSpeech(speech)

        return SpeechletResponse.newAskResponse(speech, reprompt)
    }
}
