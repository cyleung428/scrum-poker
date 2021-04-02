import { ITextFieldStyles, mergeStyles, PrimaryButton, Stack, TextField } from '@fluentui/react';
import { CompatClient, Message, Stomp } from '@stomp/stompjs';
import React, { useState } from 'react';
import SockJS from 'sockjs-client';
import './App.css';

const baseUrl = "http://localhost:8090"
const appStyles = mergeStyles({
    margin: "auto",
    width: "50%",
    padding: "50px 20px 50px 20px",
    marginTop: "100px",
    boxShadow: "0 4px 8px 0 rgba(0,0,0,0.2)"
})
const stackTokens = { childrenGap: 15 };
const textFieldStyles: Partial<ITextFieldStyles> = {};
function App() {
    const [connect, setConnect] = useState(false);
    const [email, setEmail] = useState("");
    const [stompClient, setStompClient] = useState<CompatClient>();

    const onChangeEmail = React.useCallback(
        (event: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newValue?: string) => {
            setEmail(newValue || '');
        },
        [],
    );

    const connectToServer = () => {
        const socket = new SockJS(`${baseUrl}/chat`);
        const stompClient = Stomp.over(socket);
        const onMessageReceived = (payload:Message) => {
            const message = JSON.parse(payload.body);
            console.log(message);
        }
    
        const onConnected = () => {
            stompClient.subscribe('/topic/public', onMessageReceived)
            stompClient.send("/app/chat.newUser",
                {},
                JSON.stringify({ sender: email, type: 'CONNECT' })
            )
            setConnect(true);
        }
    
        const onError = () => {
    
        }
        stompClient.connect({}, onConnected, onError)
    }
    return (
        <div className={appStyles}>
            <Stack tokens={stackTokens}>
            {
                connect ?
                    <>
                    <span>Email: {email}</span>
                    </>
                    :
                    <>
                        <TextField
                            label="Type your Email"
                            value={email}
                            onChange={onChangeEmail}
                            styles={textFieldStyles}
                        />
                        <PrimaryButton text="Connect" onClick={connectToServer} />
                    </>

            }
            </Stack>
        </div>
    );
}

export default App;
