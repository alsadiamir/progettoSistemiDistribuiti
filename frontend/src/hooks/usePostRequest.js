import {useCallback, useState} from 'react';

export const usePostRequest = (endpoint) => { 
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const doPost = useCallback((body, successData) => {
        setLoading(true)
        fetch(endpoint, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
        }).then(resp => {
            console.warn(resp)
            if (resp.ok === true) {
                if (successData) {
                    setData(successData)
                } else {
                    setData(resp.bodyUsed === true ? JSON.parse(resp.body) : null)
                }
                setError(null)
            } else {
                setData(null)
                setError(`Something went wrong when communicating with our servers (STATUS=${resp.status})`)
            }
            setLoading(false)
        }, error => {
            setData(null);
            setError(error.toString());
            setLoading(false)
        })
    }, [endpoint])

    return { doPost, data, error, loading }
}