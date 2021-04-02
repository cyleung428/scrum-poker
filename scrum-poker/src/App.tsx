import { ITextFieldStyles, mergeStyles, PrimaryButton, Stack, TextField } from '@fluentui/react';
import React, { useState } from 'react';
import './App.css';

const appStyles = mergeStyles({
    margin: "auto",
    width: "50%",
    padding: "50px 20px 50px 20px",
    marginTop: "100px",
    boxShadow: "0 4px 8px 0 rgba(0,0,0,0.2)"
})
const stackTokens = { childrenGap: 15 };
const textFieldStyles: Partial<ITextFieldStyles> = {  };
function App() {
    const [connect, setConnect] = useState(false);
    const [email, setEmail] = useState("");

    const onChangeEmail = React.useCallback(
        (event: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newValue?: string) => {
            setEmail(newValue || '');
        },
        [],
    );

    const connectToServer = () => {
        
    }
    return (
        <div className={appStyles}>
            {
                connect ?
                    <>

                    </>
                    :
                    <Stack tokens={stackTokens}>
                        <TextField
                            label="Type your Email"
                            value={email}
                            onChange={onChangeEmail}
                            styles={textFieldStyles}
                        />
                        <PrimaryButton text="Connect" onClick={connectToServer} />
                    </Stack>

            }
        </div>
    );
}

export default App;
