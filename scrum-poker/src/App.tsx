import { CompoundButton, DefaultButton, DetailsList, DetailsListLayoutMode, getTheme, IconButton, IIconProps, ITextFieldStyles, mergeStyles, mergeStyleSets, Modal, PrimaryButton, SelectionMode, Stack, TextField } from '@fluentui/react';
import { CompatClient, Message, Stomp } from '@stomp/stompjs';
import React, { useState } from 'react';
import { useBoolean } from '@fluentui/react-hooks';
import SockJS from 'sockjs-client';
import './App.css';

const baseUrl = "http://localhost:8090"
const appStyles = mergeStyles({
    margin: "auto",
    width: "50%",
    padding: "50px 20px 50px 20px",
    marginTop: "100px",
    marginBottom: "100px",
    boxShadow: "0 4px 8px 0 rgba(0,0,0,0.2)"
})
const stackTokens = { childrenGap: 15 };
const textFieldStyles: Partial<ITextFieldStyles> = {};
interface IMessage {
    content: any;
    type: String;
    sender: String;
}
interface IStory {
    id: Number;
    storyName: String;
    active: Boolean;
}
interface IStoryPoint {
    username: String;
    storyPoint: String;
}
interface IStoryPointResult {
    storyName: String;
    storyPointList: IStoryPoint[];
    time: Date;
}
const _columns = [
    { key: 'username', name: 'Username', fieldName: 'username', minWidth: 100, maxWidth: 200, isResizable: true },
    { key: 'storyPoint', name: 'Story Point', fieldName: 'storyPoint', minWidth: 100, maxWidth: 200, isResizable: true },
];

const theme = getTheme();
const contentStyles = mergeStyleSets({
    container: {
        display: 'flex',
        flexFlow: 'column nowrap',
        alignItems: 'stretch',
        minWidth: "600px"
    },
    header: [
        {
            flex: '1 1 auto',
            borderTop: `4px solid ${theme.palette.themePrimary}`,
            color: theme.palette.neutralPrimary,
            display: 'flex',
            alignItems: 'center',
            padding: '12px 12px 14px 24px',
        },
    ],
    body: {
        flex: '4 4 auto',
        padding: '0 24px 24px 24px',
        overflowY: 'hidden',
        selectors: {
            p: { margin: '14px 0' },
            'p:first-child': { marginTop: 0 },
            'p:last-child': { marginBottom: 0 },
        },
    },
});

const iconButtonStyles = {
    root: {
        color: theme.palette.neutralPrimary,
        marginLeft: 'auto',
        marginTop: '4px',
        marginRight: '2px',
    },
    rootHovered: {
        color: theme.palette.neutralDark,
    },
};

const cancelIcon: IIconProps = { iconName: 'Cancel' };

const choice = ["1", "2", "3", "5", "8", "13", "no_idea", "resign"];

function App() {
    const [connect, setConnect] = useState(false);
    const [email, setEmail] = useState("");
    const [storyList, setStoryList] = useState([] as IStory[]);
    const [activeID, setActiveID] = useState(-1);
    const [stompClient, setStompClient] = useState<CompatClient>();
    const [selectedStoryPoint, setSelectedStoryPoint] = useState<String>();
    const [storyPointResult, setStoryPointResult] = useState<IStoryPointResult>();
    const [isModalOpen, { setTrue: showModal, setFalse: hideModal }] = useBoolean(false);




    const onChangeEmail = React.useCallback(
        (event: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newValue?: string) => {
            setEmail(newValue || '');
        },
        [],
    );

    const connectToServer = () => {
        const socket = new SockJS(`${baseUrl}/chat`);
        const stompClient = Stomp.over(socket);
        const onMessageReceived = (payload: Message) => {
            const message: IMessage = JSON.parse(payload.body);
            console.log(message);
            const content = message.content;
            switch (message.type) {
                case "GETSTORY":
                    setActiveID(content.activeID);
                    setStoryList(content.storyList);
                    break;
                case "REVEALRESULT":
                    console.log(content);
                    setSelectedStoryPoint("");
                    setStoryPointResult({
                        storyName: content.storyName,
                        time: new Date(),
                        storyPointList: Object.entries(content.result).map(([key, value]) => ({
                            username: key,
                            storyPoint: value as String,
                        }))
                    });
                    showModal();
                    break;
            }
        }

        const onConnected = () => {
            stompClient.subscribe('/topic/public', onMessageReceived)
            stompClient.send("/app/chat.newUser",
                {},
                JSON.stringify({ sender: email, type: 'CONNECT' })
            )
            setConnect(true);
            setStompClient(stompClient);
        }

        const onError = () => {
            setConnect(false);
        }
        stompClient.connect({}, onConnected, onError)
    }

    const onSelectStory = (id: Number) => {
        if (stompClient) {
            stompClient.send("/app/chat.selectStory",
                {},
                JSON.stringify({ sender: email, content: id })
            )
        }
    }

    const onSelectStoryPoint = (storyPoint: String) => {
        if (stompClient) {
            stompClient.send("/app/chat.selectStoryPoint",
                {},
                JSON.stringify({ sender: email, content: storyPoint })
            )
            setSelectedStoryPoint(storyPoint);
        }
    }
    return (
        <div className={appStyles}>
            <Stack tokens={stackTokens}>
                {
                    connect ?
                        <>
                            <span>Email: {email}</span>

                            <DefaultButton onClick={showModal} text="Show Previous Result" />
                            {
                                activeID === -1 ?
                                    <>
                                        {
                                            storyList.map(story => (
                                                <CompoundButton
                                                    primary
                                                    key={`${story.id}`}
                                                    secondaryText={`ID: ${story.id}`}
                                                    onClick={() => { onSelectStory(story.id) }}
                                                >
                                                    {story.storyName}
                                                </CompoundButton>
                                            ))
                                        }
                                    </>
                                    :
                                    <>
                                        <span>Active Story: {storyList.find(story => story.id === activeID)?.storyName}</span>
                                        <div>Selected story point: {selectedStoryPoint}</div>
                                        {
                                            choice.map(choice => (
                                                <CompoundButton
                                                    primary
                                                    key={`${choice}`}
                                                    onClick={() => { onSelectStoryPoint(choice) }}
                                                >
                                                    {choice}
                                                </CompoundButton>
                                            ))
                                        }
                                    </>
                            }
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
            <Modal
                titleAriaId="result"
                isOpen={isModalOpen}
                onDismiss={hideModal}
                isBlocking={false}
                containerClassName={contentStyles.container}
            >
                <div className={contentStyles.header}>
                    <span id="header">{storyPointResult?.storyName}</span>
                    <IconButton
                        styles={iconButtonStyles}
                        iconProps={cancelIcon}
                        ariaLabel="Close popup modal"
                        onClick={hideModal}
                    />
                </div>
                <div className={contentStyles.body}>
                    <span>Time: {storyPointResult?.time.toLocaleString()}</span>
                    <DetailsList
                        items={storyPointResult?.storyPointList ? storyPointResult.storyPointList : []}
                        columns={_columns}
                        setKey="set"
                        layoutMode={DetailsListLayoutMode.justified}
                        selectionMode={SelectionMode.none}
                    />
                </div>

            </Modal>
        </div>
    );
}

export default App;

